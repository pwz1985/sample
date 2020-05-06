package org.wise.sample.concurrent.thread;

import java.util.concurrent.*;

public class ChildThread {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final ThreadLocal threadLocal = new InheritableThreadLocal();
        threadLocal.set("hello");

        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                System.out.println(1);
                Thread.sleep(1000);
                return 111;
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(10);
        Future future = executor.submit(callable);

//        Object x = future.get();
//        System.out.println(x);
        while (!future.isDone()){
            boolean done = future.isDone();
            System.out.println(done);
            Thread.sleep(100);
        }

        System.out.println(future.isDone());

//        System.out.println(future.isDone());
//
//        if(){
//            System.out.println("done");
//            executor.shutdown();
//        }


//        if(future.isDone()){
//            System.out.println("done");
//        }

//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(1);
//                System.out.println(threadLocal.get());
//
//                threadLocal.set(222);
//                System.out.println(threadLocal.get());
//
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        FutureTask future = new FutureTask(runnable, null);
//        future.run();
//
//        if (future.isDone()) {
//            System.out.println("完成");
//        } else {
//            System.out.println("未完成");
//        }
//
//        Object o = future.get();
//
//        System.out.println(o);
//
//
//        System.out.println(threadLocal.get());

    }

}
