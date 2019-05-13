package com.mengwei.app

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.mengwei.ktea.Settings
import com.mengwei.ktea.http.JsonStyle
import java.util.*

/**
 * Create by MengWei at 2019/1/28
 */


class KTApp : Application() {
    private val activitys: LinkedList<Activity> = LinkedList()

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

        // 初始化KTea的配置
        Settings.init {
            appCtx = this@KTApp
            baseUrl = "http://..."
            activityStack = activitys
            isDEBUG = BuildConfig.DEBUG
            jsonObjectStyle= JsonStyle().apply {
                statusName = "code"
                dataName = "data"
                messageName = "msg"
                successStatusValue = "1"
                tokenLoseStatusValue = "0"
            }
        }
    }

    fun exitApp() {
        for (activity in activitys) {
            activity.finish()
        }
        activitys.clear()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(packageName)
        System.exit(0)
    }
}