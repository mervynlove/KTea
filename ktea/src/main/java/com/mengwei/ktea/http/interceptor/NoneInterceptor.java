package com.mengwei.ktea.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * meng wei on 2017/4/12 15:37
 */
public class NoneInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        return chain.proceed(chain.request());
    }
}
