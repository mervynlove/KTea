package com.mengwei.ktea.ktExtends

import android.graphics.Bitmap
import com.mengwei.ktea.common.launchIO
import com.mengwei.ktea.common.launchUI
import okio.Okio
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

/**
 * Create by MengWei at 2019/5/31
 */

enum class BackStatus { SUCCESS, FAILED }

/**
 * 把bitmap保存到文件中
 * status : enum class BackStatus { SUCCESS, FAILED }
 */
fun Bitmap.save2File(file: File, backInfo: (status: BackStatus, info: String) -> Unit) {
    launchIO {
        try {
            val os = ByteArrayOutputStream()
            compress(Bitmap.CompressFormat.JPEG, 100, os)
            Okio.buffer(Okio.sink(file)).write(os.toByteArray()).close()
            launchUI {
                backInfo(BackStatus.SUCCESS, "保存成功! 路径: ${file.absolutePath}")
            }
        } catch (e: Exception) {
            launchUI {
                backInfo(BackStatus.FAILED, "保存失败, ${e.message}")
            }
        }
    }
}

/**
 * 把ByteArray保存到文件中
 * status : enum class BackStatus { SUCCESS, FAILED }
 */
fun File.saveByteArray(bytes: ByteArray, backInfo: (status: BackStatus, info: String) -> Unit) {
    launchIO {
        try {
            Okio.buffer(Okio.sink(this@saveByteArray)).write(bytes).close()
            launchUI {
                backInfo(BackStatus.SUCCESS, "保存成功! 路径: $absolutePath")
            }
        } catch (e: Exception) {
            launchUI {
                backInfo(BackStatus.FAILED, "保存失败, ${e.message}")
            }
        }
    }
}

fun File.saveSource(inputStream: InputStream, backInfo: (status: BackStatus, info: String) -> Unit) {
    launchIO {
        try {
            val sink = Okio.buffer(Okio.sink(this@saveSource))
            val source = Okio.buffer(Okio.source(inputStream))
            sink.writeAll(source)
            sink.flush()
            sink.close()
            source.close()
            launchUI {
                backInfo(BackStatus.SUCCESS, "保存成功! 路径: $absolutePath")
            }
        } catch (e: Exception) {
            launchUI {
                backInfo(BackStatus.FAILED, "保存失败, ${e.message}")
            }
        }
    }
}