
@file:Suppress("NOTHING_TO_INLINE")

import android.graphics.Matrix

/**
 * Create by MengWei at 2018/7/13
 */

inline operator fun Matrix.times(m: Matrix) = Matrix(this).apply { preConcat(m) }

inline fun Matrix.values() = FloatArray(9).apply { getValues(this) }

fun translationMatrix(tx: Float = 0.0f, ty: Float = 0.0f) = Matrix().apply { setTranslate(tx, ty) }


fun scaleMatrix(sx: Float = 1.0f, sy: Float = 1.0f) = Matrix().apply { setScale(sx, sy) }

fun rotationMatrix(degrees: Float, px: Float = 0.0f, py: Float = 0.0f) =
    Matrix().apply { setRotate(degrees, px, py) }
