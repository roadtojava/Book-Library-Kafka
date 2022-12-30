package com.learnkafka.producer;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RefernceTypes {
    public static void main(String[] args) {
        RefernceTypes refernceTypes = new RefernceTypes();
//        Obj strong = refernceTypes.create(1);
        Obj common = refernceTypes.create(2);
        Obj common1 = refernceTypes.create(3);
        Obj common2 = refernceTypes.create(4);
        WeakReference<Obj> weakReference = new WeakReference<>(common);
        SoftReference<Obj> softReference = new SoftReference<>(common1);

        ReferenceQueue referenceQueue = new ReferenceQueue();
        PhantomReference<Obj> phantomReference = new PhantomReference<Obj>(common2, referenceQueue);
//        strong=null;
        common = null;
        common1 = null;


        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < 120000; i++) {
            integerList.add(i);
        }
//
//        System.out.println("Strong "+ strong);
        int count = 0;
        while (count < 200000) {
            System.out.println(count++);
        }
//        while (weakReference.get() != null) {
//            count *= count * count;
//            System.out.print(count);
//        }
//        if (weakReference.get() == null) {
//            System.out.println("wekr nulled");
//        }
//        if (softReference.get() == null) {
//            System.out.println("soft nulled");
//        }
//        while (phantomReference.get() == null) {
//            new Date();
//        }
        common2 = null;
//        System.gc();
        System.out.println(count);
        System.out.println("Weak " + weakReference.get());
        System.out.println("Soft " + softReference.get());
//        System.out.println("Phantom "+ phantomReference.);
        System.out.println(phantomReference.isEnqueued());
        Reference<?> referenceFromQueue;
        Reference<?> referenceFromQueue1=null;
        System.out.println(phantomReference.get());
        while ((referenceFromQueue = referenceQueue.poll()) != null) {
            System.out.println("finlized999");
            referenceFromQueue1=referenceFromQueue;
//            referenceFromQueue.clear();
        }
        System.out.println(referenceFromQueue1.get());
    }

    Obj create(int i) {
        return new Obj(i);
    }

    class Obj {
        public Obj(Integer a) {
            this.a = a;
        }

        Integer a = 1;
        String b = "A";

        @Override
        public String toString() {
            return "Obj{" +
                    "a=" + a +
                    ", b='" + b + '\'' +
                    '}';
        }

//        @Override
//        protected void finalize() throws Throwable {
//            System.out.println("finalized" + a);
//            super.finalize();
//        }
    }
}
