package com.mengwei.ktea.base

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.mengwei.ktea.Settings
import com.mengwei.ktea.common.createLoadingDialog
import com.mengwei.ktea.common.delayUI
import com.mengwei.ktea.common.logger
import com.mengwei.ktea.http.TokenLose
import com.mengwei.ktea.rxbus.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Job
import java.util.concurrent.TimeUnit

/**
 * Create by MengWei at 2018/7/31
 */
abstract class BaseActivity : AppCompatActivity() {

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

}