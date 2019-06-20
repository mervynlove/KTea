package com.mengwei.ktea.common

import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by MengWei at 2019/6/20
 */
val dateFormat by lazy { SimpleDateFormat() }

fun Long.formatDateString(pattern: String): String {
    dateFormat.applyPattern(pattern)
    return dateFormat.format(Date(this))
}