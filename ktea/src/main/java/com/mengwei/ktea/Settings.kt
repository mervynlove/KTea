package com.mengwei.ktea

import android.app.Activity
import android.app.Application
import com.mengwei.ktea.http.JsonStyle
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import java.util.*

/**
 * Create by MengWei at 2018/9/7
 */


class Settings {

    companion object {
        private val settings by lazy { Settings() }
        private var isFirst = true

        fun init(config: Settings.() -> Unit) {
            if (isFirst) {
                settings.config()
                isFirst = false
            } else {
                throw RuntimeException("Settings不能多次初始化")
            }
        }

        val baseUrl by lazy { settings.baseUrl }
        fun activityStack() = settings.activityStack
        fun isDEBUG() = settings.isDEBUG
        fun appCtx() = settings.appCtx
        fun jsonObjectStyle() = settings.jsonObjectStyle
        fun jsonArrayStyle() = settings.jsonArrayStyle
    }

    lateinit var activityStack: LinkedList<Activity> //保存所有activity的栈

    var isDEBUG = false //当前是否为debug模式

    lateinit var baseUrl: String //retrofit

    lateinit var appCtx: Application

    var jsonObjectStyle: JsonStyle? = null

    var jsonArrayStyle: JsonStyle? = null

    init {
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}