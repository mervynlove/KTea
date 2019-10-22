package com.mengwei.ktea.common

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.io.File

fun ImageView.loadUrl(url: String) = Glide.with(context).load(url).into(this)
fun ImageView.loadResId(@DrawableRes resId: Int) = Glide.with(context).load(resId).into(this)
fun ImageView.loadBitmap(bitmap: Bitmap) = Glide.with(context).load(bitmap).into(this)
fun ImageView.loadDrawable(drawable: Drawable) = Glide.with(context).load(drawable).into(this)
fun ImageView.loadFile(file: File) = Glide.with(context).load(file).into(this)

fun ImageView.loadUrlToRound(url: String, radius: Int = 20) = Glide.with(context).load(url).apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))).into(this)
fun ImageView.loadResIdToRound(@DrawableRes resId: Int, radius: Int = 20) = Glide.with(context).load(resId).apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))).into(this)
fun ImageView.loadBitmapToRound(bitmap: Bitmap, radius: Int = 20) = Glide.with(context).load(bitmap).apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))).into(this)
fun ImageView.loadDrawableToRound(drawable: Drawable, radius: Int = 20) = Glide.with(context).load(drawable).apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))).into(this)
fun ImageView.loadFileToRound(file: File, radius: Int = 20) = Glide.with(context).load(file).apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))).into(this)

fun ImageView.loadUrlToCircle(url: String) = Glide.with(context).load(url).apply(multiTrans2).into(this)
fun ImageView.loadResIdToCircle(@DrawableRes resId: Int) = Glide.with(context).load(resId).apply(multiTrans2).into(this)
fun ImageView.loadBitmapToCircle(bitmap: Bitmap) = Glide.with(context).load(bitmap).apply(multiTrans2).into(this)
fun ImageView.loadDrawableToCircle(drawable: Drawable) = Glide.with(context).load(drawable).apply(multiTrans2).into(this)
fun ImageView.loadFileToCircle(file: File) = Glide.with(context).load(file).apply(multiTrans2).into(this)


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