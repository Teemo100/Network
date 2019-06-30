package com.soul.netutil.interceptor;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * description:
 * @author soul
 * Create date: 2019/7/1/001 1:43
*/
public class MockIntercepter implements Interceptor {

    private final String TAG = "MockIntercepter";
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String SDRoot = File.separator + "QMTV" + File.separator;
    private boolean isLocalMock;

    public MockIntercepter(boolean isLocalMock) {
        this.isLocalMock = isLocalMock;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(isLocalMock) {//如果开启了本地Mock，mock_modual_action.json
            List<String> pathSegments = request.url().pathSegments();
            String pathFile;
            StringBuilder stringBuilder = new StringBuilder("mock_");
            for (int i = 0; i < pathSegments.size(); i++) {
                stringBuilder.append(pathSegments.get(i));
                if(i != pathSegments.size()-1) {
                    stringBuilder.append("_");
                }
            }
            pathFile = stringBuilder.append(".json").toString();
            Log.d(TAG, "对应的Mock文件： " + pathFile);
            File file = checkSdcard(pathFile);
            if(file != null) {//查看SD卡/QMTV/mock文件夹下的txt格式的json文件 o.o
                Response response = readFile(request, file);
                if (response != null) {
                    return response;
                }
            }
        }
        return chain.proceed(request);
    }

    private File checkSdcard(String name) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File filePath = new File(sdPath + SDRoot + "mock");
            Log.d(TAG, "checkSDcard: " + filePath);
            if(filePath.exists()) {
                File[] files = filePath.listFiles();
                if(files != null) {
                    for (File file : files) {
                        if(TextUtils.equals(file.getName(), name)) {
                            Log.d(TAG, "oh, find it!!! " + filePath + File.separator + name);
                            return file;
                        }
                    }
                }
            }else {
                filePath.mkdirs();
            }
        }
        return null;
    }

    private Response readFile(Request request, File file) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str=br.readLine()) != null) {
                sb.append(str);
            }
            return setResponse(request, sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String getRequestString(Request request) {
        Buffer buffer = new Buffer();
        try {
            request.body().writeTo(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isPlaintext(buffer)) {
            return buffer.readString(UTF8);
        }
        return null;
    }

    private Response setResponse(Request request, String responseString) throws UnsupportedEncodingException {
        return new Response.Builder()
                .code(200)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message("OK")
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes("utf-8")))
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addHeader("Content-Length", ""+responseString.length())
                .addHeader("Server", "Super Mock Server @bin i love you")
                .build();
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }
}
