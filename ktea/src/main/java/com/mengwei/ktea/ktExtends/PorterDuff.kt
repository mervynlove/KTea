

@file:Suppress("NOTHING_TO_INLINE")

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.PorterDuffXfermode


/**
 * Create by MengWei at 2018/7/13
 */

inline fun PorterDuff.Mode.toXfermode() = PorterDuffXfermode(this)

inline fun PorterDuff.Mode.toColorFilter(color: Int) = PorterDuffColorFilter(color, this)
