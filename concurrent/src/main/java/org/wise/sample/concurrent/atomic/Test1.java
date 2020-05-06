
package org.wise.sample.concurrent.atomic;

import sun.misc.Unsafe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

public class Test1 {

    private AtomicBoolean atomicBoolean;

    public void test() {
        boolean b = atomicBoolean.get();
    }

    public void test1(String x) {
        System.out.println(x);
    }

    public void test1(String x,String y,Integer xxx) {
        System.out.println(x);
    }

    public static void main(String[] args) {
//                        Runnable runnable = null;
//                        V result = null;
//        Future future = new FutureTask(runnable, result);

        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        System.out.println(unsafeField);

        Annotation[] annotations = Unsafe.class.getAnnotations();
        System.out.println(annotations);

        Test1 test1 = new Test1();

    }
}
