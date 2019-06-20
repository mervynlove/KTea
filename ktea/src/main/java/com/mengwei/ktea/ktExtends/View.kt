package com.mengwei.ktea.ktExtends

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxSeekBar
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Create by MengWei at 2018/9/30
 */

infix fun View.onclick(listener: () -> Unit) {
    setOnClickListener { listener() }
}

/**
 * 2秒内的单击事件只发生一次
 */
infix fun View.singleClick(listener: (Any?) -> Unit) = RxView.clicks(this)
        .throttleFirst(2, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(listener)

fun View.singleClick(milliSeconds: Long = 2000, listener: (Any?) -> Unit) = RxView.clicks(this)
        .throttleFirst(milliSeconds, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(listener)

/**
 * 当EditText在2秒内无变化时触发
 */
infix fun TextView.changed(listener: (String) -> Unit) = RxTextView.afterTextChangeEvents(this)
        .debounce(2, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { listener(text.toString().trim()) }

fun TextView.changed(milliSeconds: Long = 2000, listener: (String) -> Unit) = RxTextView.afterTextChangeEvents(this)
        .debounce(milliSeconds, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { listener(text.toString().trim()) }

/**
 * 当EditText输入文字时键盘回车按下时触发, 每2秒可触发一次, 自动关闭键盘
 */
infix fun EditText.done(listener: (String) -> Unit) = RxTextView.editorActions(this)
        .throttleFirst(2, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            listener(text.toString().trim())
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }

fun EditText.done(milliSeconds: Long = 2000,listener: (String) -> Unit) = RxTextView.editorActions(this)
        .throttleFirst(milliSeconds, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            listener(text.toString().trim())
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }

fun SeekBar.changes(milliSeconds: Long = 200, listener: (progress: Int) -> Unit) = RxSeekBar.changes(this)
        .debounce(milliSeconds, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(listener)
