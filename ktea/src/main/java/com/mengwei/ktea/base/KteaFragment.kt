package com.mengwei.ktea.base

import Toastor
import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.mengwei.ktea.common.logger
import com.mengwei.ktea.ktExtends.activity
import com.mengwei.ktea.ktExtends.dp2px
import com.mengwei.ktea.ktExtends.px2dp
import com.mengwei.ktea.views.FancyToast

/**
 * Create by MengWei at 2018/9/3
 */
abstract class KteaFragment : Fragment() {

    protected inline fun <reified T : ViewModel> getViewModel() = ViewModelProviders.of(this).get(T::class.java)

    protected inline fun <reified T : Activity> startActivity() = activity?.run { startActivity(Intent(this, T::class.java)) }

    protected inline fun <reified T : Activity> createIntent() = activity?.run { Intent(this, T::class.java) }

    protected fun dp2px(dps: Int) = activity?.dp2px(dps) ?: 0

    protected fun px2dp(pxs: Int) = activity?.px2dp(pxs) ?: 0

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
        activity<KteaActivity> { showLoading() }
    }

    protected fun dismissLoading() {
        activity<KteaActivity> { dismissLoading() }
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logger("当前打开Fragment:${javaClass.simpleName}")
        initData()
    }

    abstract fun initData()
}