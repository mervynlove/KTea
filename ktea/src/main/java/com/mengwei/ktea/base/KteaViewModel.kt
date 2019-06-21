package com.mengwei.ktea.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import kotlinx.coroutines.Job

/**
 * Create by MengWei at 2018/8/17
 */
open class KteaViewModel : ViewModel() {
    protected val jobs by lazy { mutableListOf<Job>() }
    val errorLiveData by lazy { MutableLiveData<String>() }
    val successLiveData by lazy { MutableLiveData<String>() }

    protected fun <T> getLiveData() = KteaLiveData<T>()


    override fun onCleared() {
        close()
        super.onCleared()
    }

    protected open fun close() {
        jobs.forEach { it.cancel() }
    }
}