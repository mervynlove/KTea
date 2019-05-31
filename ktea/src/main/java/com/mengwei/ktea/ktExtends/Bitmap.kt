@file:Suppress("NOTHING_TO_INLINE")

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.support.annotation.ColorInt
import com.mengwei.ktea.common.launchIO
import com.mengwei.ktea.common.launchUI
import okio.Okio
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Create by MengWei at 2018/7/13
 */

inline fun Bitmap.applyCanvas(block: Canvas.() -> Unit): Bitmap {
    val c = Canvas(this)
    c.block()
    return this
}

inline operator fun Bitmap.get(x: Int, y: Int) = getPixel(x, y)

inline operator fun Bitmap.set(x: Int, y: Int, @ColorInt color: Int) = setPixel(x, y, color)

inline fun Bitmap.scale(width: Int, height: Int, filter: Boolean = true): Bitmap {
    return Bitmap.createScaledBitmap(this, width, height, filter)
}

inline fun createBitmap(
        width: Int,
        height: Int,
        config: Bitmap.Config = Bitmap.Config.ARGB_8888
): Bitmap {
    return Bitmap.createBitmap(width, height, config)
}

/**
 * 旋转一个图片
 * degree: 旋转角度(顺时针为正, 逆时针为负)
 */
fun Bitmap.rotateBitmap(degree: Float): Bitmap {
    val matrix = Matrix()
    matrix.setRotate(degree)
    val newBM = Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
    recycle()
    return newBM
}

/**
 * 裁剪一个图片
 * x: 左上x坐标 y: 左上y坐标 width: 长度 height: 高度
 */
fun Bitmap.crop(x: Int, y: Int, width: Int, height: Int): Bitmap {
    val newBM = Bitmap.createBitmap(this, x, y, width, height, null, false)
    recycle()
    return newBM
}

fun Bitmap.save2File(file: File, backInfo: (info: String) -> Unit) {
    launchIO {
        try {
            val os = ByteArrayOutputStream()
            compress(Bitmap.CompressFormat.JPEG, 100, os)
            Okio.buffer(Okio.sink(file)).write(os.toByteArray()).close()
            launchUI {
                backInfo("Success! Path: ${file.absolutePath}")
            }
        } catch (e: Exception) {
            launchUI {
                backInfo("Failed, ${e.message}")
            }
        }
    }
}

fun Bitmap.toByteArray(): ByteArray {
    val os = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, os)
    return os.toByteArray()
}