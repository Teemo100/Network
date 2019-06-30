package com.soul.netutil;

import com.soul.netutil.base.DefaultConfig;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.plugins.RxJavaPlugins;

/**
 * description:
 *
 * @author soul
 * Create date: 2019/6/30/030 20:55
 */
public class ApiClient {
    private static ApiClient mInstance;
    private static boolean isDebug;
    private List<ApiErrorHandler> errorHandlers;

    private ApiClient() {
        errorHandlers = new ArrayList<>();
        RxJavaPlugins.setErrorHandler(throwable -> {
            if (errorHandlers != null && errorHandlers.size() > 0) {
                for (ApiErrorHandler errorHandler : errorHandlers) {
                    errorHandler.handleError(throwable);
                }
            }
        });
    }

    private static ApiClient getInstance() {
        if (mInstance == null) {
            synchronized (ApiClient.class) {
                if (mInstance == null) {
                    mInstance = new ApiClient();
                }
            }
        }
        return mInstance;
    }

    /**
     * 在Application中初始化
     * <pre><code>
     * Api.ConfigParams params = new Api.ConfigParams();
     * params.release = isRelease();
     * params.channel = getChannel();
     * params.versionName = BuildConfig.VERSION_NAME;
     * ApiClient.initApi(params);
     * </code></pre>
     *
     * @param params Api.ConfigParams
     */
    public static ApiClient initApi(Api.ConfigParams params) {
        isDebug = params.isDebug;
        Api.init(params);
        return getInstance();
    }

    /**
     * 添加通用Api错误处理
     *
     * @param errorHandler 错误回调
     */
    public ApiClient addErrorHandler(ApiErrorHandler errorHandler) {
        mInstance.errorHandlers.add(errorHandler);
        return mInstance;
    }

    /**
     * <pre><code>
     * ApiClient.create(Service.class)
     *          .fetch()
     *          .observeOn(AndroidSchedulers.mainThread())
     *          .subscribe(ApiObserver);
     * </code></pre>
     *
     * @param service api接口 类名必须指定 以QM/SY/UDATA为后缀
     * @param <T>     代理类类型
     * @return 请求代理类
     */
    public static <T> T create(Class<T> service) {
        if (service == null) {
            throw new IllegalArgumentException("service can't be null");
        }
        ApiConfig annotation = service.getAnnotation(ApiConfig.class);
        if (annotation != null) {
            Class<? extends Api.Config> config = annotation.value();
            return Api.provide(config).create(service);
        }
        return Api.provide(DefaultConfig.class).create(service);
    }

    public interface ApiErrorHandler {

        /**
         * 处理错误回调实现
         *
         * @param throwable 异常
         */
        void handleError(Throwable throwable);
    }
}
