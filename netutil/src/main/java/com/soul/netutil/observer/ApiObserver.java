package com.soul.netutil.observer;

import android.support.annotation.NonNull;
import android.util.Log;


import com.qmtv.lib.util.NetworkHttpUtils;
import com.soul.netutil.ApiException;
import com.soul.netutil.base.BaseViewModel;
import com.soul.netutil.model.GeneralResponse;
import com.soul.netutil.util.HttpApiFailToastUtils;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * description:
 * @author soul
 * Create date: 2019/7/1/001 1:42
*/
public abstract class ApiObserver<T> extends DisposableObserver<T> {

    private boolean toastError;

    public ApiObserver() {
    }

    public ApiObserver(boolean toastError) {
        this.toastError = toastError;
    }

    public ApiObserver(BaseViewModel viewModel) {
        this(false, viewModel);
    }

    public ApiObserver(boolean toastError, BaseViewModel viewModel) {
        this(toastError);
        if (viewModel != null) {
            viewModel.add(this);
        }else {
            e("viewModel is null");
        }
    }

    @Override
    public void onStart() {
        if (!NetworkHttpUtils.isNetworkConnected()) {
            dispose();
        }
        if (!isDisposed()) {
            onBefore();
        }
    }

    @Override
    public void onNext(T response) {
        try {
            assertSuccessful(response);
        } catch (Exception e) {
            onError(e);
        }
    }

    @Deprecated
    @Override
    public void onError(Throwable throwable) {
        e("It is not a crash, just print StackTrace, please check it \n"
                + Log.getStackTraceString(throwable));
        try {
            if (toastError) {
                HttpApiFailToastUtils.showError(throwable);
            }
            RxJavaPlugins.getErrorHandler().accept(throwable);
        } catch (Exception e) {
            e("It is not a crash, just print StackTrace, please check it \n"
                    + Log.getStackTraceString(e));
        } finally {
            onFail(throwable);
        }
    }

    @Override
    public void onComplete() {
    }

    private void assertSuccessful(T response) {
        if (response == null) {
            throw new ApiException("response is null");
        }
        if (response instanceof GeneralResponse) {
            GeneralResponse resp = (GeneralResponse) response;

            if (!isSuccess(resp)) {
                if (onAssert(response)) {
                    return;
                }
                throw new ApiException(resp.message, resp.code);
            }
        }
        onSuccess(response);
    }

    private boolean isSuccess(@NonNull GeneralResponse response) {
        return isSuccessful(response) && response.data != null;
    }

    protected boolean isSuccessful(@NonNull GeneralResponse response) {
        return response.code == 0 || response.code == 200;
    }

    public void onBefore() {
    }

    /**
     * <p>当response code不是常规码时，会回调fail，如果是特殊业务场景code，可以自己处理掉</p>
     * <p>此方法适用于非常规业务码处理</p>
     * <code>
     *     <pre>
     *         public boolean onAssert(GeneralResponse response) {
     *              if (response.code == 100) {
     *                  ...
     *                  return true;
     *              }
                   return false;
               }
     *     </pre>
     * </code>
     * @param resp GeneralResponse
     * @return true能处理，不会回调onFail, false不处理
     */
    public boolean onAssert(@NonNull T resp) {
        return false;
    }

    public abstract void onSuccess(@NonNull T resp);

    public void onFail(Throwable throwable) {
    }

    private void e(String msg) {
        Log.wtf("ApiObserver", msg);
    }
}
