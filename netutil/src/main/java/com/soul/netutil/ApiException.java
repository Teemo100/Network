package com.soul.netutil;

import android.support.annotation.NonNull;
import com.soul.netutil.model.GeneralResponse;

/**
 * description:
 * @author soul
 * Create date: 2019/7/1/001 0:49
*/
public class ApiException extends RuntimeException {

    public int code = -1;

    public ApiException(@NonNull GeneralResponse response) {
        this(response.message, response.code);
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public ApiException(String detailMessage, int code) {
        super(detailMessage);
        this.code = code;
    }
}
