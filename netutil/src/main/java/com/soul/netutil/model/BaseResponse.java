package com.soul.netutil.model;

import android.support.annotation.Nullable;

/**
 * description:
 * @author soul
 * Create date: 2019/7/1/001 0:27
*/
public class BaseResponse<T> {
    @Nullable
    public T data;
    public int status;
    public Throwable throwable;

    @Nullable
    public T getData() {
        return data;
    }
}
