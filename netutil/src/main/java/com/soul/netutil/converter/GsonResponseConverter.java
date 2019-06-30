package com.soul.netutil.converter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.soul.netutil.model.BaseResponse;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * description:Gson转化器实现
 * @author soul
 * Create date: 2019/7/1/001 0:27
*/
public class GsonResponseConverter<T> implements Converter<ResponseBody, T> {

    private final Type[] type;

    public GsonResponseConverter(Type... type) {
        this.type = type;
    }

    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {

        try {
            if (type != null) {
                if (type.length == 1 && type[0] == String.class) {
                    return (T) value.string();
                }
                Gson gson = new Gson();
                JsonReader jsonReader = gson.newJsonReader(value.charStream());
                TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type[0]));
                T t = (T) adapter.read(jsonReader);
                if (t instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) t;
                    if (response.data != null && response.data instanceof LinkedTreeMap && type.length > 1) {
                        String str = gson.toJson(response.data);
                        response.data = gson.fromJson(str, type[1]);
                    }
                }
                return t;
            }
            String responseString = value.string();
            return (T) responseString;
        } finally {
            value.close();
        }
    }
}
