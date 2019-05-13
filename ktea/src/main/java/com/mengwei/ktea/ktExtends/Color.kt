

@file:Suppress("NOTHING_TO_INLINE", "WRONG_ANNOTATION_TARGET_WITH_USE_SITE_TARGET_ON_TYPE")

import android.graphics.Color
import android.support.annotation.ColorInt

/**
 * Create by MengWei at 2018/7/13
 */

inline val @receiver:ColorInt Int.alpha get() = (this shr 24) and 0xff

inline val @receiver:ColorInt Int.red get() = (this shr 16) and 0xff

inline val @receiver:ColorInt Int.green get() = (this shr 8) and 0xff

inline val @receiver:ColorInt Int.blue get() = this and 0xff

inline operator fun @receiver:ColorInt Int.component1() = (this shr 24) and 0xff

inline operator fun @receiver:ColorInt Int.component2() = (this shr 16) and 0xff

inline operator fun @receiver:ColorInt Int.component3() = (this shr 8) and 0xff

inline operator fun @receiver:ColorInt Int.component4() = this and 0xff

@ColorInt
inline fun String.toColorInt(): Int = Color.parseColor(this)
