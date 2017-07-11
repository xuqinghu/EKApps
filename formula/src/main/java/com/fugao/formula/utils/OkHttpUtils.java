package com.fugao.formula.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class OkHttpUtils {
    public static final String TAG = "OkHttpUtils";
    public static OkHttpUtils mInstance;
    public OkHttpClient mOkHttpClient;
    public Handler mDelivery;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String BASE_URL = "";

    private OkHttpUtils() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(15, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
    }

    private synchronized static OkHttpUtils getmInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpUtils();
        }
        return mInstance;
    }

    private void getRequest(String url, final ResultCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        deliveryResult(callback, request);
    }

    private void postRequest(String url, final ResultCallback callback, String json) {
        Request request = buildPostRequest(url, json);
        deliveryResult(callback, request);
    }

    private void deliveryResult(final ResultCallback callback, Request request) {

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                sendFailCallback(callback, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String str = response.body().string();
                    int code = response.code();
                    if (callback.mType == String.class) {
                        sendSuccessCallBack(callback, str, code);
                    }
                } catch (final Exception e) {
                    sendFailCallback(callback, e);
                }

            }
        });
    }

    public void sendFailCallback(final ResultCallback callback, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
    }

    public void sendSuccessCallBack(final ResultCallback callback, final Object obj, final int code) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(obj, code);
                }
            }
        });
    }

    public Request buildPostRequest(String url, String json) {
        RequestBody requestBody = RequestBody.create(JSON, json);
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * 将网路请求转换为 绝对地址
     *
     * @param relativeUrl
     * @return
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


    /**********************对外接口************************/

    /**
     * get请求
     *
     * @param url      请求url
     * @param callback 请求回调
     */
    public static void get(String url, ResultCallback callback) {
        getmInstance().getRequest(getAbsoluteUrl(url), callback);
    }

    /**
     * post请求
     *
     * @param url      请求url
     * @param callback 请求回调
     * @param json     请求参数
     */
    public static void post(String url, final ResultCallback callback, String json) {
        getmInstance().postRequest(getAbsoluteUrl(url), callback, json);
    }

    /**
     * http请求回调类,回调方法在UI线程中执行
     *
     * @param <T>
     */
    public static abstract class ResultCallback<T> {

        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        /**
         * 请求成功回调
         *
         * @param response
         */
        public abstract void onSuccess(T response, int code);

        /**
         * 请求失败回调
         *
         * @param e
         */
        public abstract void onFailure(Exception e);
    }
}
