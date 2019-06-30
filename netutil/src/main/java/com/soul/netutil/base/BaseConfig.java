package com.soul.netutil.base;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.soul.netutil.Api;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * description: 基础配置
 * @author soul
 * Create date: 2019/7/1/001 1:40
*/
public abstract class BaseConfig implements Api.Config {

    protected Api.ConfigParams params;
    public static boolean online;

    @Override
    public void build(@NonNull Retrofit.Builder builder, @NonNull Api.ConfigParams params) {
        this.params = params;
        online = params.isOnline;
        builder.client(getClient());
        if (!TextUtils.isEmpty(getBaseUrl())) {
            builder.baseUrl(getBaseUrl());
        } else {
            builder.baseUrl(getBaseHttpUrl());
        }
        addConverterFactory(builder);
        addCallAdapterFactory(builder);
    }

    private void addCallAdapterFactory(Retrofit.Builder builder) {
        List<CallAdapter.Factory> factories = getCallAdapterFactories();
        if (factories != null) {
            for (CallAdapter.Factory factory : factories) {
                builder.addCallAdapterFactory(factory);
            }
        }
    }

    private void addConverterFactory(Retrofit.Builder builder) {
        List<Converter.Factory> factories = getConverterFactories();
        if (factories != null) {
            for (Converter.Factory factory : factories) {
                builder.addConverterFactory(factory);
            }
        }
    }

    public abstract OkHttpClient getClient();

    public String getBaseUrl() {
        return null;
    }

    public HttpUrl getBaseHttpUrl() {
        return null;
    }

    public abstract List<Converter.Factory> getConverterFactories();

    public abstract List<CallAdapter.Factory> getCallAdapterFactories();

}
