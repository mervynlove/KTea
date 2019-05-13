package com.mengwei.ktea.common

import java.security.MessageDigest


/**
 * Create by MengWei at 2018/11/28
 */

fun String.MD5() = MD5.encode(this)

private object MD5 {
    fun encode(text: String): String {
        if (text.isEmpty()) return ""
        val instance: MessageDigest = MessageDigest.getInstance("MD5")
        val digest: ByteArray = instance.digest(text.toByteArray())
        val sb = StringBuilder()
        for (b in digest) {
            val i: Int = b.toInt() and 0xff
            var hexString = Integer.toHexString(i)
            if (hexString.length == 1) {
                hexString = "0$hexString"
            }
            sb.append(hexString)
        }
        return sb.toString()
    }
}