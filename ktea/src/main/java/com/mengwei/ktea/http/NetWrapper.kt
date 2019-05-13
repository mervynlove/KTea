package com.mengwei.ktea.http

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

/**
 * 当时FILE的时候上传文件参数value必须为File类型或者string类型!!!
 */
enum class Method { GET, POST, FILE }

class TokenLose

class NetWrapper<T> {
    lateinit var url: String
    var method = Method.GET
    var params: MutableMap<String, Any?>? = null
        get() = field ?: mutableMapOf()

    lateinit var onSuccess: (result: T) -> Unit
    lateinit var onError: (error: String) -> Unit
}

object HTTP {
    fun execute(method: Method, url: String, params: MutableMap<String, Any?>?): Response<ResponseBody> {
        if (params == null) throw java.lang.IllegalArgumentException("请求参数params不能为null!!!")
        val api = NetClient.getClient().create(API::class.java)
        return when (method) {
            Method.GET -> api.getCall(url, params)
            Method.POST -> api.postCall(url, params)
            //上传文件参数必须为File类型或者string类型
            Method.FILE -> {
                val map = mutableMapOf<String, RequestBody>()
                for (param in params) {
                    val value = param.value
                    if (value != null) {
                        when (value) {
                            is File -> map["${param.key}\"; filename=\"${value.name}"] =
                                    RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), value)
                            is String -> map[param.key] = RequestBody.create(MediaType.parse("text/plain"), value)
                            is Int -> map[param.key] =
                                    RequestBody.create(MediaType.parse("text/plain"), value.toString())
                            else -> throw IllegalArgumentException("上传文件请求的参数值必须为File/String/Int类型!!!")
                        }
                    }

                }
                api.fileCall(url, map)
            }
        }.execute()
    }
}