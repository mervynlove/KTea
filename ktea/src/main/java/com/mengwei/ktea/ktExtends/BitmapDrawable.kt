
@file:Suppress("NOTHING_TO_INLINE", "WRONG_ANNOTATION_TARGET_WITH_USE_SITE_TARGET_ON_TYPE")

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable

/**
 * Create by MengWei at 2018/7/13
 */

inline fun Bitmap.toDrawable(resources: Resources) = BitmapDrawable(resources, this)
