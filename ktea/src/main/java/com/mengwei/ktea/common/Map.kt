package com.mengwei.ktea.common

/**
 * Create by MengWei at 2019/6/19
 */

fun <K, V> LinkedHashMap<K, V>.head() = get(keys.first())

fun <K, V> LinkedHashMap<K, V>.last() = get(keys.last())