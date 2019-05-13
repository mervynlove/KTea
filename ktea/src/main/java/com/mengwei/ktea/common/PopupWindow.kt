package com.mengwei.ktea.common

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.mengwei.ktea.R

/**
 * Create by MengWei at 2018/8/23
 */

/**
 * 构建一个在view下方并且高度包裹内容的popupwindow
 */
fun createWrapHeightPopwindow(contentView: View) =
        PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true).apply {
            animationStyle = R.style.AnimationTopFade
            val dw = ColorDrawable(Color.TRANSPARENT)
            setBackgroundDrawable(dw)
            isOutsideTouchable = true
        }

/**
 * 构建一个在view下方并且高度填充父布局的popupwindow
 */
fun createMatchHeightPopWindow(contentView: View) =
        PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true).apply {
            animationStyle = R.style.AnimationTopFade
            val dw = ColorDrawable(Color.TRANSPARENT)
            setBackgroundDrawable(dw)
            isOutsideTouchable = true
        }

/**
 * 在目标view的下方显示popupwindow
 */
fun PopupWindow.showDown(targetView: View) {
    if (height == ViewGroup.LayoutParams.WRAP_CONTENT || Build.VERSION.SDK_INT < 24) {
        showAsDropDown(targetView)
    } else {
        val visibleFrame = Rect()
        targetView.getGlobalVisibleRect(visibleFrame)
        val height = targetView.resources.displayMetrics.heightPixels - visibleFrame.bottom
        this.height = height
        showAsDropDown(targetView)
    }
}
