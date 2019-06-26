package com.mengwei.ktea.common

/**
 * Create by MengWei at 2018/9/14
 */

// 是否是邮箱格式
fun String.isEmail() = matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))

//只含有数字
fun String.isOnlyNumbers() = matches(Regex("\\d+"))

//不含有小写字母
fun String.isNotIncludeLowerCase() = matches(Regex("[A-Z0-9]+"))

//不含有大写字母
fun String.isNotIncludeUpperCase() = matches(Regex("[a-z0-9]+"))

//不含有数字
fun String.isNotIncludeOneNumber() = !matches(Regex(".*\\d.*"))

//含有数字
fun String.isIncludeOneNumber() = matches(Regex(".*\\d.*"))

//同时含有数字和字母
fun String.isIncludeNumberAndLetter()= matches(Regex(".*\\d.*[A-Za-z].*|.*[A-Za-z].*\\d.*"))

//以数字开头
fun String.isStartsWithNumber() = Character.isDigit(this[0])

//是手机号
fun String.isMobileNumber() = matches(Regex("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$"))
