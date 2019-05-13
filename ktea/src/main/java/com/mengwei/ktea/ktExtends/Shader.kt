

import android.graphics.Matrix
import android.graphics.Shader

/**
 * Create by MengWei at 2018/7/13
 */

inline fun Shader.transform(block: Matrix.() -> Unit) {
    val matrix = Matrix()
    getLocalMatrix(matrix)
    block(matrix)
    setLocalMatrix(matrix)
}
