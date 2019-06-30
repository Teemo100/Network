package com.soul.netutil;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import retrofit2.Retrofit;

/**
 * description:Api执行代理
 * @author soul
 * Create date: 2019/7/1/001 1:42
*/
public final class ApiProxy {

    private final ArrayMap<Class, Object> apiCache;
    private Retrofit retrofit;
    private Lock lock = new ReentrantLock();

    ApiProxy(@NonNull Retrofit retrofit) {
        apiCache = new ArrayMap<>();
        this.retrofit = retrofit;
    }

    public <T> T create(Class<T> tClass) {
        Object service = apiCache.get(tClass);
        if (service == null) {
            lock.lock();
            service = retrofit.create(tClass);
            apiCache.put(tClass, service);
            lock.unlock();
        }
        return (T) service;
    }

    public <T> RequestBuilder<T> newBuilder(RequestBuilder.Adapter<T> adapter) {
        return new RequestBuilder<>(adapter);
    }
}