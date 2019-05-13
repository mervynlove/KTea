package com.mengwei.ktea.http

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


/**
 * Create by MengWei at 2018/7/25
 */
interface API {

/*    @GET
    fun<T> getBaseData(@Url url: String, @QueryMap params: MutableMap<String, Any?>): Observable<BaseInfo<T>>

    @FormUrlEncoded
    @POST
    fun<T> postBaseData(@Url url: String, @FieldMap params: MutableMap<String, Any?>): Observable<BaseInfo<T>>*/

/*    @GET
    fun getRawData(@Url url: String, @QueryMap params: MutableMap<String, Any?>): Observable<ResponseBody>

    @FormUrlEncoded
    @POST
    fun postRawData(@Url url: String, @FieldMap params: MutableMap<String, Any?>): Observable<ResponseBody>*/

    @GET
    fun getCall(@Url url: String, @QueryMap params: MutableMap<String, Any?>): Call<ResponseBody>

    @FormUrlEncoded
    @POST
    fun postCall(@Url url: String, @FieldMap params: MutableMap<String, Any?>): Call<ResponseBody>

    @Multipart
    @POST
    fun fileCall(@Url url: String, @PartMap params: MutableMap<String, RequestBody>): Call<ResponseBody>

}