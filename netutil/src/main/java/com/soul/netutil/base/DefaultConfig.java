package com.soul.netutil.base;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.soul.netutil.base.BaseConfig;
import com.soul.netutil.interceptor.MockIntercepter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * description:默认配置
 * @author soul
 * Create date: 2019/7/1/001 1:28
*/
public class DefaultConfig extends BaseConfig {

    public static final String NETWORK_TIMEOUT = "NETWORK_TIMEOUT";

    public static final long TIMEOUT_SHORT = 15000;
    public static final long TIMEOUT_NORMAL = 30000;
    public static final long TIMEOUT_LONG = 60000;

    @Override
    public OkHttpClient getClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.MINUTES))
                .connectTimeout(6L, TimeUnit.MILLISECONDS)
                .readTimeout(getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(getWriteTimeout(), TimeUnit.MILLISECONDS);
        addInterceptor(okHttpClientBuilder);
        if (params.isDebug) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(new MockIntercepter(true));
            okHttpClientBuilder.addNetworkInterceptor(httpLoggingInterceptor);
            okHttpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
        }
        return okHttpClientBuilder.build();
    }

    protected long getReadTimeout() {
        return TIMEOUT_NORMAL;
    }

    protected long getWriteTimeout() {
        return TIMEOUT_NORMAL;
    }

    protected void addInterceptor(OkHttpClient.Builder okHttpClientBuilder) {
    }

    @Override
    public HttpUrl getBaseHttpUrl() {
        HttpUrl.Builder builder = new HttpUrl.Builder();
        if (online) {
            builder.scheme("https")
                    .host("www.quanmin.tv");
        } else {
            builder.scheme("http")
                    .host("test-www.quanmin.tv");
        }
        return builder.build();
    }

    @Override
    public List<Converter.Factory> getConverterFactories() {
        List<Converter.Factory> factories = new ArrayList<>();
        factories.add(GsonConverterFactory.create());
        return factories;
    }

    @Override
    public List<CallAdapter.Factory> getCallAdapterFactories() {
        List<CallAdapter.Factory> factories = new ArrayList<>();
        factories.add(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));
        return factories;
    }
}