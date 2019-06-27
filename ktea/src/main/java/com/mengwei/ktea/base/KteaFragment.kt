package com.mengwei.ktea.base

import Toastor
import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.mengwei.ktea.common.logger
import com.mengwei.ktea.views.FancyToast

/**
 * Create by MengWei at 2018/9/3
 */
abstract class KteaFragment : Fragment() {

    protected inline fun <reified T : ViewModel> getViewModel() = ViewModelProviders.of(this).get(T::class.java)

    protected inline fun <reified T : ViewModel> getActivityViewModel() = ViewModelProviders.of(kteaActivity).get(T::class.java)

    protected inline fun <reified T : Activity> startActivity() = activity?.run { startActivity(Intent(this, T::class.java)) }

    protected inline fun <reified T : Activity> newIntent() = activity?.run { Intent(this, T::class.java) }

    protected val kteaActivity: KteaActivity by lazy {
        val thisActivity = activity
        if (thisActivity is KteaActivity) thisActivity
        else throw IllegalStateException("${javaClass.simpleName}的activity不是KteaActivity")
    }

    protected inline fun <reified T : FragmentActivity> kteaActivity(runnable: T.() -> Unit) {
        val thisActivity = activity
        if (thisActivity is T) {
            thisActivity.runnable()
        } else throw IllegalStateException("${javaClass.simpleName}的activity不是${T::class.java.simpleName}")
    }

    protected fun infoToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        activity?.run {
            Toastor.info?.cancel()
            Toastor.info = FancyToast.makeText(this, text, duration, FancyToast.INFO)
            Toastor.info?.run {
                setGravity(Gravity.CENTER, 0, 200)
                show()
            }
        }
    }

    protected fun errorToast(text: String, duration: Int = Toast.LENGTH_LONG) {
        activity?.run {
            Toastor.error?.cancel()
            Toastor.error = FancyToast.makeText(this, text, duration, FancyToast.ERROR)
            Toastor.error?.run {
                setGravity(Gravity.CENTER, 0, 200)
                show()
            }
        }
    }

    protected fun successToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        activity?.run {
            Toastor.success?.cancel()
            Toastor.success = FancyToast.makeText(this, text, duration, FancyToast.SUCCESS)
            Toastor.success?.run {
                setGravity(Gravity.CENTER, 0, 200)
                show()
            }
        }
    }

    protected fun warningToast(text: String, duration: Int = Toast.LENGTH_LONG) {
        activity?.run {
            Toastor.warning?.cancel()
            Toastor.warning = FancyToast.makeText(this, text, duration, FancyToast.WARNING)
            Toastor.warning?.run {
                setGravity(Gravity.CENTER, 0, 200)
                show()
            }
        }
    }


    protected fun showLoading() {
        kteaActivity.showLoading()
    }

    protected fun dismissLoading() {
        kteaActivity.dismissLoading()
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logger("当前打开Fragment:${javaClass.simpleName}")
        initData()
    }

    abstract fun initData()
}