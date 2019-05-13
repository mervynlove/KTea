package com.mengwei.ktea.ktExtends

import android.database.sqlite.SQLiteDatabase

/**
 * Create by MengWei at 2018/7/13
 */

inline fun <T> SQLiteDatabase.transaction(
        exclusive: Boolean = true,
        body: SQLiteDatabase.() -> T
): T {
    if (exclusive) {
        beginTransaction()
    } else {
        beginTransactionNonExclusive()
    }
    try {
        val result = body()
        setTransactionSuccessful()
        return result
    } finally {
        endTransaction()
    }
}