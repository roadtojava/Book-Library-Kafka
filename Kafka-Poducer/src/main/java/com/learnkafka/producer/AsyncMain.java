package com.learnkafka.producer;

import com.learnkafka.domain.Book;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class AsyncMain {
    public static void main1(String[] args) {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("in the run");
            }
        });
        completableFuture.join();
        System.out.println("after");
    }

    public static void main(String[] args) {

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = 5;
        int y = 5;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(x, y));

        JButton button = new JButton("Do it");
        button.setPreferredSize(new Dimension(100, 100));
        panel.add(button);

        JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        container.add(panel);
        JScrollPane scrollPane = new JScrollPane(container);
        f.getContentPane().add(scrollPane);

        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        button.addActionListener(e -> {
            System.out.println("before");
            try {
//            completableFuture();
//                for (int i = 0; i <5 ; i++) {
//                System.out.println(completableFutureSupplier().join());
                test();
                System.out.println("!!! ");
//                }
//                Thread.sleep(6000);
                System.out.println("after");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

    }

    public static void test() {
        try {
            System.out.println("------------------0");
            CompletableFuture.allOf(completableFuture());
            System.out.println("------------------2");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static CompletableFuture<String> completableFutureSupplier() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                    System.out.println("Run async");
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("after sleep");
                    return "Supplier";
                }
        );
        return completableFuture;
    }

    public static CompletableFuture<Void> completableFuture() throws ExecutionException, InterruptedException {
//       return CompletableFuture.allOf(CompletableFuture.runAsync(() -> {
//            System.out.println("Run async");
//            try {
//                Thread.sleep(5000);
//                System.out.println("after sleep");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
////            return "ok";
//        })).join();
        return CompletableFuture.runAsync(() -> {
            System.out.println("Run async");
            try {
                Thread.sleep(5000);
                System.out.println("after sleep");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
