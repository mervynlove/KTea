package com.mengwei.ktea.common

import kotlinx.coroutines.*

/**
 * Create by MengWei at 2018/9/25
 */

fun launchUI(block: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(Dispatchers.Main, block = block)

fun launchIO(block: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(Dispatchers.IO, block = block)

fun <T> asyncUI(block: suspend CoroutineScope.() -> T) = GlobalScope.async(Dispatchers.Main, block = block)

fun <T> asyncIO(block: suspend CoroutineScope.() -> T) = GlobalScope.async(Dispatchers.IO, block = block)

fun delayUI(milliSeconds: Long, doing: () -> Unit) = GlobalScope.launch(Dispatchers.Main) {
    delay(milliSeconds)
    doing()
}

