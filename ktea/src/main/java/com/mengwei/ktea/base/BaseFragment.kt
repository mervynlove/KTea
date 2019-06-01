package com.mengwei.ktea.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.mengwei.ktea.common.logger
import com.mengwei.ktea.ktExtends.activity

/**
 * Create by MengWei at 2018/9/3
 */
abstract class BaseFragment : Fragment() {

    fun showLoading() {
        activity<BaseActivity> { showLoading() }
    }

    fun dismissLoading() {
        activity<BaseActivity> { dismissLoading() }
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logger("当前打开Fragment:${javaClass.simpleName}")
        initData()
    }

    abstract fun initData()
}