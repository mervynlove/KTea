package com.mengwei.ktea.base

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

/**
 * Create by MengWei at 2019/6/21
 */
class KteaLiveData<T> : LiveData<T>() {

    public override fun postValue(value: T) {
        super.postValue(value)
    }

    public override fun setValue(value: T) {
        super.setValue(value)
    }

    fun observe(owner: LifecycleOwner, observer: (T) -> Unit) {
        super.observe(owner, Observer {
            it?.run { observer(it) }
        })
    }

}