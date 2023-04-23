package com.zj.android_asm;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public  class MyThreadPool {


    public static Executor singlePool = Executors.newSingleThreadExecutor();

    public static Executor fixPool = Executors.newFixedThreadPool(8, new ThreadFactory() {

        private final AtomicInteger mThreadId = new AtomicInteger(0);

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("MyThreadPool-"+this.mThreadId.getAndIncrement());
            return t;
        }
    });

}
