package com.mengwei.ktea.common

import com.alibaba.fastjson.JSON
import org.json.JSONArray
import org.json.JSONObject

/**
 * Create by MengWei at 2018/8/30
 */

inline fun <reified T> String.toEntity() = JSON.parseObject(this, T::class.java)

inline fun <reified T> String.toEntityList() = JSON.parseArray(this, T::class.java)

fun String.toJsonObject() = JSONObject(this)

fun String.toJsonArray() = JSONArray(this)