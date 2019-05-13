package com.mengwei.ktea.http.interceptor;

import com.mengwei.ktea.http.HttpHead;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * meng wei on 2017/4/11 17:43
 */
public class HttpHeadInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        for (Map.Entry<String, String> entry : HttpHead.INSTANCE.getParams().entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        return chain.proceed(builder.build());
    }
}
