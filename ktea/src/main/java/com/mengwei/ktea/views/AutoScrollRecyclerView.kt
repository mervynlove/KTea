package com.mengwei.ktea.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.MotionEvent
import com.mengwei.ktea.common.launchUI
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * 横向自动滚动的RecyclerView, 不需要设置LayoutManager,通过start方法直接调用
 * Create by MengWei at 2018/9/28
 */
class AutoScrollRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var canRun = false
    private val job by lazy {
        launchUI {
            while (isActive) {
                if (canRun) {
                    scrollBy(2, 0)
                }
                delay(20) //每秒钟刷新50次
            }
        }
    }

    /**
     * 开始滚动的方法.
     * lines: 显示的行数.
     */
    fun start(lines: Int) {
        layoutManager = StaggeredGridLayoutManager(lines, StaggeredGridLayoutManager.HORIZONTAL)
        canRun = true
        job.start()
    }

    /**
     * 停止滚动的方法
     */
    fun stop() {
        canRun = false
    }

    override fun onDetachedFromWindow() {
        canRun = false
        job.cancel()
        super.onDetachedFromWindow()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> canRun = false
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> canRun = true
        }
        return super.onTouchEvent(e)
    }
}