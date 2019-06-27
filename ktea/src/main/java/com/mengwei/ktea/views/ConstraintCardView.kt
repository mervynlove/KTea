package com.mengwei.ktea.views

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.TypedValue
import com.mengwei.ktea.R
import com.mengwei.ktea.ktExtends.dp2px

/**
 * Create by MengWei at 2019/6/27
 */
@RequiresApi(Build.VERSION_CODES.M)
class ConstraintCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        setBackgroundResource(R.drawable.like_cardview_bg)
        elevation = context.dp2px(10).toFloat()
        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
        val resourceId = typedValue.resourceId
        foreground = context.getDrawable(resourceId)
    }
}