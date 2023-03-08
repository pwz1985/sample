package wise;

import com.intellij.codeInsight.hint.ShowParameterInfoContext;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.lang.Language;
import com.intellij.lang.parameterInfo.LanguageParameterInfo;
import com.intellij.lang.parameterInfo.ParameterInfoHandler;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.infos.CandidateInfo;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoFillCallArguments extends PsiElementBaseIntentionAction {

    public void invoke(@NotNull final Project project, final Editor editor, @NotNull final PsiElement psiElement) throws IncorrectOperationException {
        PsiDocumentManager.getInstance(project).commitAllDocuments();

        final PsiMethodCallExpression call = PsiTreeUtil.getParentOfType(psiElement, PsiMethodCallExpression.class);
        if (call == null) {
            return;
        }

        PsiMethod psiMethod = call.resolveMethod();
        if (psiMethod == null) {
            psiMethod = resolveMethodFromCandidates(project, editor, psiElement);
            if (psiMethod == null) {
                return;
            }
        }
        final PsiParameterList parameterList = psiMethod.getParameterList();
        final PsiParameter[] params = parameterList.getParameters();

        // 当前光标的offset
        int offset = editor.getCaretModel().getOffset();
        // 当前表达式的offset
        int callTextOffset = call.getTextOffset();

        final Document doc = editor.getDocument();

        int lineNumber = doc.getLineNumber(offset);
        // 当前行的开始的offset
        int lineStartOffset = doc.getLineStartOffset(lineNumber);

        Set<String> psiVariableList = new HashSet<>();
        this.getLocalVariableList(psiVariableList, call);

        System.out.println(psiVariableList);

        // 声明变量部分
        String declareVariable = this.genDeclareVariable(params, callTextOffset - lineStartOffset, psiVariableList);

        doc.insertString(lineStartOffset, declareVariable);

        String prefix = "";
        offset = offset + declareVariable.length();

        for (final PsiParameter p : params) {
            doc.insertString(offset, prefix + p.getName());
            offset += p.getName().length() + prefix.length();
            prefix = ", ";
        }
        editor.getCaretModel().moveToOffset(offset + 1);
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        PsiMethodCallExpression parentOfType = PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression.class);
        boolean available = parentOfType != null;
        return available;
    }

    @Nls
    @NotNull
    public String getFamilyName() {
        return getText();
    }

    @NotNull
    public String getText() {
        return "Auto fill call parameters";
    }


    private PsiMethod resolveMethodFromCandidates(@NotNull final Project project, final Editor editor, @NotNull final PsiElement psiElement) {
        final PsiFile file = project == null ? null : PsiUtilBase.getPsiFileInEditor(editor, project);

        final int offset = editor.getCaretModel().getOffset();
        final int fileLength = file.getTextLength();

        final ShowParameterInfoContext context = new ShowParameterInfoContext(
                editor,
                project,
                file,
                offset,
                -1,
                false,
                false
        );

        final int offsetForLangDetection = offset > 0 && offset == fileLength ? offset - 1 : offset;
        final Language language = PsiUtilCore.getLanguageAtOffset(file, offsetForLangDetection);
        ParameterInfoHandler[] handlers = getHandlers(project, language, file.getViewProvider().getBaseLanguage());

        if (handlers == null) handlers = new ParameterInfoHandler[0];

        PsiMethod psiMethod = null;
        int mostNumberOfParameters = 0;
        DumbService.getInstance(project).setAlternativeResolveEnabled(true);
        try {
            for (final ParameterInfoHandler handler : handlers) {
                final Object element = handler.findElementForParameterInfo(context);
                if (element != null) {
                    final Object[] itemsToShow = context.getItemsToShow();
                    for (final Object item : itemsToShow) {
                        if (item instanceof CandidateInfo) {
                            final CandidateInfo candidate = (CandidateInfo) item;
                            final PsiElement candidateElement = candidate.getElement();
                            if (candidateElement instanceof PsiMethod) {
                                final PsiMethod candidatePsiMethod = (PsiMethod) candidateElement;
                                final PsiParameterList parameterList = candidatePsiMethod.getParameterList();
                                if (parameterList != null) {
                                    final PsiParameter[] params = parameterList.getParameters();
                                    if (params != null) {
                                        if (params.length > mostNumberOfParameters) {
                                            mostNumberOfParameters = params.length;
                                            psiMethod = candidatePsiMethod;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return psiMethod;
        } finally {
            DumbService.getInstance(project).setAlternativeResolveEnabled(false);
        }
    }

    @Nullable
    public static ParameterInfoHandler[] getHandlers(final Project project, final Language... languages) {
        final Set<ParameterInfoHandler> handlers = new LinkedHashSet<>();
        final DumbService dumbService = DumbService.getInstance(project);
        for (final Language language : languages) {
            handlers.addAll(dumbService.filterByDumbAwareness(LanguageParameterInfo.INSTANCE.allForLanguage(language)));
        }
        if (handlers.isEmpty()) return null;
        return handlers.toArray(new ParameterInfoHandler[0]);
    }

    private String genDeclareVariable(PsiParameter[] params, int offsetForLineHead, Set<String> psiVariableList) {
        String allGenerateStr = "";
        for (final PsiParameter p : params) {
            PsiType type = p.getType();
            String presentableText = type.getPresentableText();
            String defaultValue;

            if (psiVariableList.contains(p.getName())) {
                continue;
            }

            if (type.isAssignableFrom(PsiType.BYTE) ||
                    type.isAssignableFrom(PsiType.CHAR) ||
                    type.isAssignableFrom(PsiType.SHORT) ||
                    type.isAssignableFrom(PsiType.INT)) {
                defaultValue = "0";
            } else if (type.isAssignableFrom(PsiType.FLOAT)) {
                defaultValue = "0f";
            } else if (type.isAssignableFrom(PsiType.LONG)) {
                defaultValue = "0l";
            } else if (type.isAssignableFrom(PsiType.DOUBLE)) {
                defaultValue = "0d";
            } else if (type.isAssignableFrom(PsiType.BOOLEAN)) {
                defaultValue = "false";
            } else {
                defaultValue = "null";
            }

            String generateFormat = "%s %s = %s;";
            String declareVal = String.format(generateFormat, presentableText, p.getName(), defaultValue);
            String generateStr = StringUtils.repeat(" ", offsetForLineHead) + declareVal + "\n";
            allGenerateStr = allGenerateStr + generateStr;
        }

        return allGenerateStr;
    }

    private void getLocalVariableList(Set<String> psiVariableList, PsiElement element) {
        PsiElement parent = element.getParent();
        System.out.println(parent);
        if (parent == null) {
        } else if (PsiMethod.class.isAssignableFrom(element.getClass())) {
            PsiMethod psiMethod = (PsiMethod) element;
            PsiParameterList parameterList = psiMethod.getParameterList();
            if (!parameterList.isEmpty()) {
                PsiParameter[] parameters = parameterList.getParameters();
                Set<String> parameterNameList = Arrays.stream(parameters).map(item -> item.getName()).collect(Collectors.toSet());
                System.out.println(parameterNameList);
                psiVariableList.addAll(parameterNameList);
            }
            getLocalVariableList(psiVariableList, parent);
        } else {
            PsiElement[] children = parent.getChildren();
            Stream<PsiElement> psiElementStream = Arrays.stream(children)
                    .filter(item -> PsiDeclarationStatement.class.isAssignableFrom(item.getClass()));
            Set<String> psiElements = psiElementStream
                    .flatMap(item -> Arrays.stream(((PsiDeclarationStatement) item).getDeclaredElements()))
                    .filter(item -> PsiVariable.class.isAssignableFrom(item.getClass()))
                    .map(item -> ((PsiVariable) item).getName())
                    .collect(Collectors.toSet());
            psiVariableList.addAll(psiElements);
            getLocalVariableList(psiVariableList, parent);
        }
    }
}