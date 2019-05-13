package com.mengwei.ktea.http

import android.content.Context
import com.mengwei.ktea.Settings

/**
 * Create by MengWei at 2019/5/13
 */
object Token {
    private const val NAME = "token"
    private const val tokenKey = "tokenKey"
    private const val tokenValue = "tokenValue"
    private val SP by lazy {
        Settings.appCtx().getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    /**
     * 清空token
     */
    fun clearToken() {
        val key = SP.getString(tokenKey, "")
        if (key.isNotEmpty()) {
            val value = SP.getString(tokenValue, "")
            if (value.isNotEmpty()) {
                HttpHead.params.remove(key)
                SP.edit().apply {
                    remove(tokenKey)
                    remove(tokenValue)
                    apply()
                    commit()
                }
            }
        }
    }

    /**
     * 获取和设置token
     * pair.first : token的key String
     * pair.second : token的value String
     */
    var token: Pair<String, String>?
        get() {
            val key = SP.getString(tokenKey, "")
            val value = SP.getString(tokenValue, "")
            if (key.isNotEmpty() && value.isNotEmpty()) {
                HttpHead.params[key] = value
                return Pair(key, value)
            }
            return null
        }
        set(value) {
            if (value != null) {
                HttpHead.params[value.first] = value.second
                SP.edit().apply {
                    putString(tokenKey, value.first)
                    putString(tokenValue, value.second)
                    apply()
                    commit()
                }
            }
        }
}