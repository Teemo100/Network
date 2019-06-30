package com.soul.netutil;

import android.support.v4.util.ArrayMap;

import java.util.Map;
import java.util.Set;

/**
 * description:请求构建者
 * @author soul
 * Create date: 2019/7/1/001 1:43
*/
public class RequestBuilder<T> {

    public String url;
    public String method;
    public Map<String, String> paths;
    public Map<String, String> params;
    public Map<String, String> headers;
    public Object body;
    public Adapter<T> adapter;

    public RequestBuilder(Adapter<T> adapter) {
        this.adapter = adapter;
    }

    public RequestBuilder<T> url(String url) {
        this.url = url;
        return this;
    }

    public RequestBuilder<T> method(String method) {
        this.method = method;
        return this;
    }

    public RequestBuilder<T> path(String path, String value) {
        if (paths == null) {
            paths = new ArrayMap<>();
        }
        this.paths.put(path, value);
        return this;
    }

    public RequestBuilder<T> param(String path, String value) {
        if (params == null) {
            params = new ArrayMap<>();
        }
        this.params.put(path, value);
        return this;
    }

    public RequestBuilder<T> header(String key, String value) {
        if (headers == null) {
            headers = new ArrayMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    public RequestBuilder<T> headers(Map<String, String> value) {
        if (headers == null) {
            headers = new ArrayMap<>();
        }
        this.headers.putAll(value);
        return this;
    }

    public RequestBuilder<T> params(Map<String, String> value) {
        if (params == null) {
            params = new ArrayMap<>();
        }
        this.params.putAll(value);
        return this;
    }

    public RequestBuilder<T> body(Object value) {
        this.body = value;
        return this;
    }

    private void checkPath() {
        if (paths != null) {
            Set<Map.Entry<String, String>> set = paths.entrySet();
            for (Map.Entry<String, String> path : set) {
                url = url.replace("{" + path.getKey() + "}", path.getValue());
            }
        }
    }

    public T build() {
        checkPath();
        return adapter.adapt(this);
    }

    public interface Adapter<T> {
        T adapt(RequestBuilder<T> builder);
    }
}