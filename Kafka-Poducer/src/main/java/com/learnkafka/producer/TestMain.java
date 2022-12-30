package com.learnkafka.producer;

import java.util.concurrent.*;

public class TestMain {
    static boolean b = false;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<String> callable = () -> {
            // Perform some computation
            System.out.println("Entered Callable");
//            Thread.sleep(2000);
            return "Hello from Callable";
        };

        System.out.println("Submitting Callable");
        Future<String> future = executorService.submit(callable);

        // This line executes immediately
        System.out.println("Do something else while callable is getting executed");

        System.out.println("Retrieve the result of the future");
        // Future.get() blocks until the result is available
        String result = future.get();
        System.out.println(result);

        executorService.shutdown();
    }

    public static void main1(String[] args) throws Exception {
        TestMain tm = new TestMain();
        System.out.println("before");
//1
//        Thread thread = tm.doAsyncThread();
//        thread.start();
//1
        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Future future =
//        executorService.submit(tm.stringCallable());
        executorService.submit(() -> System.out.println("asdasd"));
//        System.out.println(future.get());
//executorService.shutdown();
//        future.get();
//        while (!future.isDone()) {
//            System.out.println("not done");
//        }
//        System.out.println(future.get());
//        System.out.println("after");
//        executorService.shutdown();
//        System.out.println(response);
    }

    public Callable<String> stringCallable() {
        Callable<String> stringCallable = () -> {
            Thread.sleep(500);
            System.out.println("int he callable");
            return "My Str";
        };
        return stringCallable;
    }

    public Thread doAsyncThread() {
        Runnable runnable = () -> {
//            b = true;
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("In the runnable");
        };
        return new Thread(runnable);
    }

    public CompletableFuture<Boolean> completableFuture() {
//    CompletableFuture.
        return null;
    }
}
