package com.tj24.base.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CacheThreadPool {

    private static volatile CacheThreadPool instance;

    private ExecutorService executorService;

    public CacheThreadPool() {
        executorService = Executors.newCachedThreadPool();
    }

     public static CacheThreadPool getInstance() {
        if(instance == null){
            synchronized (CacheThreadPool.class){
                if(instance == null){
                    instance = new CacheThreadPool();
                }
            }
        }
        return instance;
    }

    public void execute(Runnable runnable){
        executorService.execute(runnable);
    }

    public Future<?> submit(Runnable runnable){
       return executorService.submit(runnable);
    }

    public Future<?> submit(Callable callable){
        return executorService.submit(callable);
    }
}
