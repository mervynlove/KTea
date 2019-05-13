package com.mengwei.ktea.common

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.util.*

/**
 * Create by MengWei at 2018/9/10
 */

object DeviceInfo {

    private var id: String? = null
    private const val fileName = "deviceId"

    fun getDeviceId(context: Context): String? {
        if (id == null) {
            id = try {
                val file = File(context.filesDir, fileName)
                if (!file.exists()) {
                    writeInstanceFile(file)
                }
                readInstanceFile(file)
            } catch (e: Exception) {
                null
            }
        }
        return id
    }


    private fun readInstanceFile(file: File): String {
        val f = RandomAccessFile(file, "r")
        val bytes = ByteArray(f.length().toInt())
        f.readFully(bytes)
        f.close()
        return String(bytes)
    }

    private fun writeInstanceFile(file: File) {
        val outputStream = FileOutputStream(file)
        val uuid = UUID.randomUUID().toString().replace("-", "")
        outputStream.write(uuid.toByteArray())
        outputStream.close()
    }

}