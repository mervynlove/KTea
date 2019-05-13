package com.mengwei.ktea.http

import android.support.v4.util.ArrayMap
import com.mengwei.ktea.Settings
import com.mengwei.ktea.http.interceptor.HttpHeadInterceptor
import com.mengwei.ktea.http.interceptor.HttpLoggingInterceptor
import com.mengwei.ktea.http.interceptor.NoneInterceptor
import okhttp3.*
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit

object NetClient {

    fun getClient() = retrofitClient

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cache(Cache(File(Settings.appCtx().cacheDir, "HttpResponseCache"), 50 * 1024 * 1024))
                .cookieJar(object : CookieJar {
                    var cookieStore = ArrayMap<HttpUrl, List<Cookie>?>()
                    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>?) {
                        cookieStore[url] = cookies
                    }

                    override fun loadForRequest(url: HttpUrl?): List<Cookie> {
                        return cookieStore[url] ?: mutableListOf()
                    }

                })
                .addInterceptor(HttpHeadInterceptor())
                .addInterceptor(if (Settings.isDEBUG()) HttpLoggingInterceptor() else NoneInterceptor())
                .build()
    }

    /**
     * 最新使用的是手动fastjson解析和协程网络请求, 所以不需要这两种adapter了
     */
    private val retrofitClient: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(Settings.baseUrl)
                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}