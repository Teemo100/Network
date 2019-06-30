package com.soul.netutil.model;

/**
 * description:响应实体类
 * @author soul
 * Create date: 2019/7/1/001 0:28
*/
public class GeneralResponse<T> extends BaseResponse<T> {
    public int code;
    public String message;
    public String error;

    @Override
    public String toString() {
        return "GeneralResponse{" +
                "code=" + code +
                ", message='" + message +
                ", data='" + data +
                '}';
    }
}