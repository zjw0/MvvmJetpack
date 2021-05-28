package com.mvvm.libnetwork;

import androidx.annotation.IntDef;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class Request<T,R> {
    protected String mUrl;
    protected HashMap<String, String> headers = new HashMap<>();
    protected HashMap<String, Object> params = new HashMap<>();

    //仅仅只访问本地缓存，即便本地缓存不存在，也不会发起网络请求
    public static final int CACHE_ONLY = 1;
    //先访问缓存，同时发起网络的请求，成功后缓存到本地
    public static final int CACHE_FIRST = 2;
    //仅仅只访问服务器，不存任何存储
    public static final int NET_ONLY = 3;
    //先访问网络，成功后缓存到本地
    public static final int NET_CACHE = 4;
    private String cacheKey;

    @IntDef({CACHE_ONLY, CACHE_FIRST, NET_CACHE, NET_ONLY})
    public @interface CacheStrategy{

    }

    public Request(String url){
        mUrl = url;
    }

    public R addHeader(String key, String value){
        headers.put(key,value);
        return (R) this;
    }
    public R addParam(String key, Object value) {
        if (value == null) {
            return (R) this;
        }
        //int byte char short long double float boolean 和他们的包装类型，但是除了 String.class 所以要额外判断
        try {
            if (value.getClass() == String.class) {
                params.put(key, value);
            } else {
                Field field = value.getClass().getField("TYPE");
                Class claz = (Class) field.get(null);
                if (claz.isPrimitive()) {
                    params.put(key, value);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (R) this;
    }

    public R cacheKey(String key) {
        this.cacheKey = key;
        return (R) this;
    }

    public void execute(final JsonCallback<T> callback){
        getCall().enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ApiResponse<T> response = new ApiResponse<>();
                response.message = e.getMessage();
                callback.onError(response);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ApiResponse<T> apiResponse = parseResponse(response,callback);
                if(apiResponse.success){
                    callback.onError(apiResponse);
                }else {
                    callback.onSuccess(apiResponse);
                }
            }
        });
    }

    private ApiResponse<T> parseResponse(Response response, JsonCallback<T> callback) {
        String message = null;
        int status = response.code();
        boolean success = response.isSuccessful();
        ApiResponse<T> result = new ApiResponse<>();
        Convert convert =ApiService.sConvert;
        try{
            String content = response.body().string();
            if(success){
                if(callback != null){
                    ParameterizedType type = (ParameterizedType) callback.getClass().getGenericSuperclass();
                    Type argument = type.getActualTypeArguments()[0];
                    result.body = (T) convert.convert(content, argument);
                }
            }else {
                message = content;
            }
        } catch (Exception e) {
            message = e.getMessage();
            success = false;
        }
        result.success = success;
        result.status = status;
        result.message = message;
        return result;
    }

    private Call getCall() {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        addHeaders(builder);
        okhttp3.Request request = generateRequest(builder);
        Call call = ApiService.okHttpClient.newCall(request);
        return call;
    }

    protected abstract okhttp3.Request generateRequest(okhttp3.Request.Builder builder);

    private void addHeaders(okhttp3.Request.Builder builder) {
        for(Map.Entry<String, String> entry : headers.entrySet()){
            builder.addHeader(entry.getKey(), entry.getValue());

        }
    }

    public void execute(){

    }


}
