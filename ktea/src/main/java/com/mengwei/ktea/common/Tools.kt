package com.mengwei.ktea.common

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.style.Circle
import com.mengwei.ktea.R
import com.mengwei.ktea.Settings
import com.orhanobut.logger.Logger
import java.util.concurrent.locks.Lock


fun createLoadingViewInFrameLayout(ctx: Context) = SpinKitView(ctx).apply {
    layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT).apply {
        gravity = Gravity.CENTER
    }
    setIndeterminateDrawable(Circle().apply {
        color = resources.getColor(R.color.loading_view)
    })
    visibility = View.GONE
}

/**
 * 创建loading加载对话框
 */
fun createLoadingDialog(ctx: Context, cancelListener: () -> Unit): Dialog {
    return Dialog(ctx, R.style.LightProgressDialog).apply {
        val spinKitView = SpinKitView(ctx).apply {
            setIndeterminateDrawable(Circle().apply {
                color = resources.getColor(R.color.loading_view)
            })
        }
        setContentView(spinKitView)
//        setContentView(R.layout.loading_dialog)
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        val lp = window.attributes
        lp.gravity = Gravity.CENTER
        lp.dimAmount = 0.2f
        window.attributes = lp
        setOnDismissListener { cancelListener }
    }
}

/**
 * 打印log
 */
fun logger(string: String) {
    if (Settings.isDEBUG()) {
        Logger.d(string)
    }
}

/**
 * 在一个范围映射一个值到另一个范围
 */
fun mapValueFromRangeToRange(
        value: Double,
        fromLow: Double,
        fromHigh: Double,
        toLow: Double,
        toHigh: Double): Double {
    val fromRangeSize = fromHigh - fromLow
    val toRangeSize = toHigh - toLow
    val valueScale = (value - fromLow) / fromRangeSize
    return toLow + valueScale * toRangeSize
}

fun mapValueFromRangeToRange(
        value: Int,
        fromLow: Int,
        fromHigh: Int,
        toLow: Int,
        toHigh: Int): Double {
    val fromRangeSize = fromHigh - fromLow
    val toRangeSize = toHigh - toLow
    val valueScale = (value - fromLow).toDouble() / fromRangeSize
    return toLow + valueScale * toRangeSize
}

fun mapValueFromRangeToRange(
        value: Float,
        fromLow: Float,
        fromHigh: Float,
        toLow: Float,
        toHigh: Float): Double {
    val fromRangeSize = fromHigh - fromLow
    val toRangeSize = toHigh - toLow
    val valueScale = (value - fromLow).toDouble() / fromRangeSize
    return toLow + valueScale * toRangeSize
}

/**
 *  多线程中通过锁同步
 */
fun <T> locked(lock: Lock, body: () -> T): T {
    lock.lock()
    try {
        return body()
    } finally {
        lock.unlock()
    }
}
