
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.Px

/**
 * Create by MengWei at 2018/7/13
 */

fun Drawable.toBitmap(
        @Px width: Int = intrinsicWidth,
        @Px height: Int = intrinsicHeight,
        config: Config? = null
): Bitmap {
    if (this is BitmapDrawable) {
        if (config == null || bitmap.config == config) {
            if (width == intrinsicWidth && height == intrinsicHeight) {
                return bitmap
            }
            return Bitmap.createScaledBitmap(bitmap, width, height, true)
        }
    }

    val (oldLeft, oldTop, oldRight, oldBottom) = bounds

    val bitmap = Bitmap.createBitmap(width, height, config ?: Config.ARGB_8888)
    setBounds(0, 0, width, height)
    draw(Canvas(bitmap))

    setBounds(oldLeft, oldTop, oldRight, oldBottom)
    return bitmap
}

fun Drawable.updateBounds(
    @Px left: Int = bounds.left,
    @Px top: Int = bounds.top,
    @Px right: Int = bounds.right,
    @Px bottom: Int = bounds.bottom
) {
    setBounds(left, top, right, bottom)
}
