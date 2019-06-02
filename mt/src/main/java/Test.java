import org.apache.ibatis.io.VFS;

import java.io.IOException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {

        List<String> list = VFS.getInstance().list("org/apache");

        System.out.println(list);

    }
}
