package com.mengwei.ktea.http

import com.mengwei.ktea.Settings
import com.mengwei.ktea.common.*
import com.mengwei.ktea.rxbus.RxBus
import kotlinx.coroutines.Job

/**
 * Create by MengWei at 2018/8/30
 * 踩坑后重构的网络连接库:
 * 1. 使用协程实现
 * 2. 使用fastjson手动解析, 可以直接使用BaseInfo类二次泛型得到实体类
 * 3. entity只能使用java类, 因为自动生成的kotlin没有空构造方法, fastjson无法解析
 * 4. 上传文件参数必须为File类型或者string类型
 */

fun http(config: NetWrapper<String>.() -> Unit): Job {
    val wrapper = NetWrapper<String>()
    wrapper.config()
    with(wrapper) {
        return launchIO {
            var result: String? = null
            var error: String? = null
            try {
                val response = HTTP.execute(method, url, params)
                if (response.isSuccessful) {
                    result = response.body()!!.string().trim()
                } else {
                    error = "code:${response.code()} ; 连接错误:${response.errorBody()!!.string()}"
                }
            } catch (e: Exception) {
                error = "错误:${e.message}"
            }

            launchUI {
                result?.let { onSuccess(it) }
                error?.let { onError(it) }
            }.join()
        }
    }
}

//这里的泛型是返回数据解析后的Entity
inline fun <reified T> httpEntity(config: NetWrapper<T>.() -> Unit): Job {
    val wrapper = NetWrapper<T>()
    wrapper.config()
    with(wrapper) {
        return launchIO {
            var result: T? = null
            var error: String? = null
            try {
                val response = HTTP.execute(method, url, params)
                if (response.isSuccessful) {
                    val bodyString = response.body()!!.string().trim()
                    result = bodyString.toEntity<T>()
                } else {
                    error = "code:${response.code()} ; 连接错误:${response.errorBody()!!.string()}"
                }
            } catch (e: Exception) {
                error = "错误:${e.message}"
            }
            launchUI {
                result?.let { onSuccess(it) }
                error?.let { onError(it) }
            }.join()
        }
    }
}

//这里的泛型是返回数据解析后的EntityList
inline fun <reified T> httpEntityList(config: NetWrapper<List<T>>.() -> Unit): Job {
    val wrapper = NetWrapper<List<T>>()
    wrapper.config()
    with(wrapper) {
        return launchIO {
            var result: List<T>? = null
            var error: String? = null
            try {
                val response = HTTP.execute(method, url, params)
                if (response.isSuccessful) {
                    val bodyString = response.body()!!.string().trim()
                    result = bodyString.toEntityList<T>()
                } else {
                    error = "code:${response.code()} ; 连接错误:${response.errorBody()!!.string()}"
                }
            } catch (e: Exception) {
                error = "错误:${e.message}"
            }
            launchUI {
                result?.let { onSuccess(it) }
                error?.let { onError(it) }
            }.join()
        }
    }
}

/**
 * 这里的泛型是BaseInfo中的info解析后的object
 */
inline fun <reified T> httpBase(config: NetWrapper<T>.() -> Unit) = httpBase(Settings.jsonObjectStyle(), config)

inline fun <reified T> httpBase(style: JsonStyle?, config: NetWrapper<T>.() -> Unit): Job {
    if (style == null) throw IllegalArgumentException("http请求解析成指定的实体类必须指定JsonStyle!!!")
    val wrapper = NetWrapper<T>()
    wrapper.config()
    with(wrapper) {
        return launchIO {
            var result: T? = null
            var error: String? = null
            try {
                val response = HTTP.execute(method, url, params)
                if (response.isSuccessful) {
                    val json = response.body()!!.string().trim().toJsonObject()
                    val status = json.getString(style.statusName)
                    if (style.tokenLoseStatusValue != null) {
                        if (status == style.tokenLoseStatusValue) RxBus.BUS.post(TokenLose())
                    }
                    if (status == style.successStatusValue) {
                        val dataString = json.getString(style.dataName).trim()
                        result = if (dataString.isNotEmpty()) dataString.toEntity<T>() else null
                    } else {
                        error = json.getString(style.messageName)
                    }

                } else {
                    error = "code:${response.code()} ; 连接错误:${response.errorBody()!!.string()}"
                }
            } catch (e: Exception) {
                error = "错误:${e.message}"
            }
            launchUI {
                result?.let { onSuccess(it) }
                error?.let { onError(it) }
            }.join()
        }
    }
}

/**
 * 这里的泛型是BaseInfo中的info解析后生成的List<T> : 主要为了防止json数组和json对象解析成实体类/实体类数组时服务器端定义的key不一致.
 * 1. 如果设置了arrayStyle, 按照arrayStyle进行解析
 * 2. 没有设置arrayStyle, 默认按照objectStyle进行解析
 * 3. 如果两个style都没设置, 则报出异常, 根据异常可以去配置style了
 */
inline fun <reified T> httpBaseList(config: NetWrapper<List<T>>.() -> Unit): Job {
    return if (Settings.jsonArrayStyle() == null) {
        if (Settings.jsonObjectStyle() == null) {
            throw IllegalArgumentException("http请求解析成指定的实体类必须指定JsonStyle!!!")
        } else {
            httpBaseList(Settings.jsonObjectStyle(), config)
        }
    } else {
        httpBaseList(Settings.jsonArrayStyle(), config)
    }
}

inline fun <reified T> httpBaseList(style: JsonStyle?, config: NetWrapper<List<T>>.() -> Unit): Job {
    if (style == null) throw IllegalArgumentException("http请求解析成指定的实体类必须指定JsonStyle!!!")
    val wrapper = NetWrapper<List<T>>()
    wrapper.config()
    with(wrapper) {
        return launchIO {
            var result: List<T>? = null
            var error: String? = null
            try {
                val response = HTTP.execute(method, url, params)
                if (response.isSuccessful) {
                    val json = response.body()!!.string().trim().toJsonObject()
                    val status = json.getString(style.statusName)
                    if (style.tokenLoseStatusValue != null) {
                        if (status == style.tokenLoseStatusValue) RxBus.BUS.post(TokenLose())
                    }
                    if (status == style.successStatusValue) {
                        val dataString = json.getString(style.dataName).trim()
                        result = if (dataString.isNotEmpty()) dataString.toEntityList<T>() else null
                    } else {
                        error = json.getString(style.messageName)
                    }

                } else {
                    error = "code:${response.code()} ; 连接错误:${response.errorBody()!!.string()}"
                }
            } catch (e: Exception) {
                error = "错误:${e.message}"
            }
            launchUI {
                result?.let { onSuccess(it) }
                error?.let { onError(it) }
            }.join()
        }
    }
}