package com.mengwei.ktea.common


/**
 * Create by MengWei at 2018/11/28
 */

fun String.MD5() = if (isEmpty()) "" else StringDigest.of(this).md5()

fun String.SHA1() = if (isEmpty()) "" else StringDigest.of(this).sha1()

fun String.SHA256() = if (isEmpty()) "" else StringDigest.of(this).sha256()

fun String.SHA512() = if (isEmpty()) "" else StringDigest.of(this).sha512()

