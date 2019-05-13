package com.mengwei.ktea.common

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

/**
 * Create by MengWei at 2018/12/18
 */
// 圆形变换
fun RequestBuilder<Drawable>.toCircle() = this.apply(RequestOptions.circleCropTransform())

// 圆角变换
private val roundedCorners by lazy { RoundedCorners(25) }
fun RequestBuilder<Drawable>.toRound() = this.apply(RequestOptions.bitmapTransform(roundedCorners))

// 居中剪裁
fun RequestBuilder<Drawable>.cropCenter() = this.apply(RequestOptions.centerCropTransform())

// 居中圆角剪裁
private val multiTrans1 by lazy {
    RequestOptions().transform(MultiTransformation(CenterCrop(), roundedCorners))
}
fun RequestBuilder<Drawable>.cropCenterToRound() = this.apply(multiTrans1)

// 居中圆形剪裁
private val multiTrans2 by lazy {
    RequestOptions().transform(MultiTransformation(CenterCrop(), CircleCrop()))
}
fun RequestBuilder<Drawable>.cropCenterToCircle() = this.apply(multiTrans2)