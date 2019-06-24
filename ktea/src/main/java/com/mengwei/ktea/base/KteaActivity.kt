package com.mengwei.ktea.base

import Toastor
import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.Toast
import com.mengwei.ktea.Settings
import com.mengwei.ktea.common.createLoadingDialog
import com.mengwei.ktea.common.logger
import com.mengwei.ktea.http.TokenLose
import com.mengwei.ktea.rxbus.RxBus
import com.mengwei.ktea.views.FancyToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

/**
 * Create by MengWei at 2018/7/31
 */
abstract class KteaActivity : AppCompatActivity() {

    protected val disposables by lazy { CompositeDisposable() }

    @Volatile
    private var showLoadingTimes = 0

    private val loadingProgress by lazy {
        createLoadingDialog(this) { showLoadingTimes = 0 }
    }

    companion object {
        val loginObservable by lazy {
            RxBus.BUS.toObservable(TokenLose::class.java)
                    .throttleFirst(10, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())!!
        }
    }

    inline fun <reified T : Activity> createIntent() = Intent(this, T::class.java)

    inline fun <reified T : Activity> startActivity() = startActivity(Intent(this, T::class.java))

    inline fun <reified T : ViewModel> getViewModel() = ViewModelProviders.of(this).get(T::class.java)

    fun dp2px(dps: Int) = Math.round(resources.displayMetrics.density * dps)

    fun px2dp(pxs: Int) = Math.round(pxs / resources.displayMetrics.density)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Settings.activityStack().add(this)
        val handler = Handler()
        window.decorView.post {
            handler.post {
                initData()
            }
        }
        disposables.add(
                loginObservable.subscribe { tokenLoseDoing() }
        )
        logger("当前打开Activity:${javaClass.name}")
    }

    // 显示加载框
    fun showLoading() {
        ++showLoadingTimes
        if (!loadingProgress.isShowing) {
            loadingProgress.show()
        }
    }

    // 关闭加载框
    fun dismissLoading() {
        if (showLoadingTimes > 0) {
            --showLoadingTimes
        }
        if (showLoadingTimes == 0) {
            if (loadingProgress.isShowing) loadingProgress.dismiss()
        }
    }

    /**
     * 本方法初始化数据
     */
    abstract fun initData()

    /**
     * token丢失时需要要做的事情
     */
    open fun tokenLoseDoing() = Unit

    override fun onDestroy() {
        if (loadingProgress.isShowing) {
            loadingProgress.cancel()
        }
        Settings.activityStack().remove(this)
        disposables.clear()
        super.onDestroy()
    }

    fun infoToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        Toastor.info?.cancel()
        Toastor.info = FancyToast.makeText(this, text, duration, FancyToast.INFO)
        Toastor.info?.run {
            setGravity(Gravity.CENTER, 0, 200)
            show()
        }
    }

    fun errorToast(text: String, duration: Int = Toast.LENGTH_LONG) {
        Toastor.error?.cancel()
        Toastor.error = FancyToast.makeText(this, text, duration, FancyToast.ERROR)
        Toastor.error?.run {
            setGravity(Gravity.CENTER, 0, 200)
            show()
        }
    }

    fun successToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        Toastor.success?.cancel()
        Toastor.success = FancyToast.makeText(this, text, duration, FancyToast.SUCCESS)
        Toastor.success?.run {
            setGravity(Gravity.CENTER, 0, 200)
            show()
        }
    }

    fun warningToast(text: String, duration: Int = Toast.LENGTH_LONG) {
        Toastor.warning?.cancel()
        Toastor.warning = FancyToast.makeText(this, text, duration, FancyToast.WARNING)
        Toastor.warning?.run {
            setGravity(Gravity.CENTER, 0, 200)
            show()
        }
    }

}