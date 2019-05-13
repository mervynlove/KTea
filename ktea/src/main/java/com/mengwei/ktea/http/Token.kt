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
     * 获取和设置token
     */
    var token: Pair<String, String>
        get() = Pair(
                SP.getString(tokenKey, ""),
                SP.getString(tokenValue, "")
        )
        set(value) {
            HttpHead.params[value.first] = value.second
            SP.edit().apply {
                putString(tokenKey, value.first)
                putString(tokenValue, value.second)
                apply()
                commit()
            }
        }
}