package org.wise.sample.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExecutor1 {

    public void test1() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Runnable task = () -> System.out.println(1);
        executorService.submit(task);

        executorService.shutdown();
    }

    public static void main(String[] args) {
        ThreadExecutor1 threadExecutor1 = new ThreadExecutor1();
        threadExecutor1.test1();
    }
}
