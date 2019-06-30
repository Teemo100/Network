package com.soul.netutil.util;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.qmtv.lib.util.ToastUtil;
import com.soul.netutil.BaseApplication;
import com.soul.network.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * description:
 *
 * @author soul
 * Create date: 2019/7/1/001 0:40
 */
public class HttpApiFailToastUtils {

    public static void showError(@Nullable Throwable throwable) {
        showError(throwable, R.string.network_request_error);
    }

    public static void showError(@Nullable Throwable throwable, @StringRes int message) {
        showError(throwable, BaseApplication.getContext().getString(message));
    }

    /**
     * showError 红色背景的Toast，以强调提示
     *
     * @param throwable 异常
     * @param message   默认信息
     */
    public static void showError(@Nullable Throwable throwable, String message) {
        if (throwable instanceof ConnectException || throwable instanceof UnknownHostException) {
            ToastUtil.showToast(R.string.toast_service_connect_fail);
            return;
        }
        if (throwable instanceof SocketTimeoutException) {
            ToastUtil.showToast(R.string.toast_response_time_out);
            return;
        }
        if (throwable instanceof HttpException || throwable instanceof JsonParseException) {
            ToastUtil.showToast(R.string.toast_service_error);
            return;
        }
        if (!TextUtils.isEmpty(throwable.getMessage())) {
            ToastUtil.showToastError(throwable.getMessage());
            return;
        }
        ToastUtil.showToast(message, Toast.LENGTH_LONG);
    }

    private static void showDebug(@Nullable Throwable throwable, String message) {
        ToastUtil.showToast(
                BaseApplication.getContext()
                , ">>> Don't be Confused, Debug ONLY <<<"
                        + "\n\nmessage: " + message
                        + "\n\nthrowable: " + ((null == throwable) ? "null" : throwable)
                , Toast.LENGTH_LONG);
    }
}
