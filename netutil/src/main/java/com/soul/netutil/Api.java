package com.soul.netutil;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import retrofit2.Retrofit;

/**
 * description:网络基础类
 * 在Application中调用Api.init(params)初始化后使用</p>
 * @author soul
 * Create date: 2019/7/1/001 1:40
*/
public final class Api {

    private static Lock lock = new ReentrantLock();
    private static ArrayMap<String, ApiProxy> apiProxies = new ArrayMap<>();
    private static ConfigParams params;

    private Api() {
    }

    /**
     * 在Application中初始化
     * <pre><code>
     * Api.ConfigParams params = new Api.ConfigParams();
     * params.release = isRelease();
     * params.channel = getChannel();
     * params.versionName = BuildConfig.VERSION_NAME;
     * Api.init(params);
     * </code></pre>
     *
     * @param configParams ConfigParams
     */
    public static void init(ConfigParams configParams) {
        params = configParams;
    }

    public static ApiProxy provide(Class<? extends Config> clientConfig) {
        if (clientConfig == null) {
            throw new IllegalArgumentException("config can't be null");
        }
        String clientName = clientConfig.getCanonicalName();
        ApiProxy apiProxy = apiProxies.get(clientName);
        if (apiProxy == null) {
            apiProxy = new ApiProxy(newRetrofit(clientConfig));
            lock.lock();
            apiProxies.put(clientName, apiProxy);
            lock.unlock();
        }
        return apiProxy;
    }

    private static Retrofit newRetrofit(@NonNull Class<? extends Config> configClass) {
        Retrofit.Builder builder = new Retrofit.Builder();
        try {
            configClass.newInstance().build(builder, params);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static synchronized void clear() {
        apiProxies.clear();
    }

    /**
     * 配置参数
     *
     * @author bin
     * @date 2017/12/8 11:51
     */
    public static class ConfigParams {
        public boolean isDebug;
        public boolean isOnline;
        public String versionName;
        public String channel;

        @Override
        public String toString() {
            return "ConfigParams{" +
                    "isDebug=" + isDebug +
                    ", isOnline=" + isOnline +
                    ", versionName='" + versionName + '\'' +
                    ", channel='" + channel + '\'' +
                    '}';
        }
    }

    /**
     * 配置接口
     *
     * @author bin
     * @date 2017/12/8 11:51
     */
    public interface Config {
        void build(Retrofit.Builder builder, ConfigParams params);
    }
}
