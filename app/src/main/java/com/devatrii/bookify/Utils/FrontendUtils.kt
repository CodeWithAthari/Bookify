package com.devatrii.bookify.Utils

import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.ViewTarget

fun ImageView.loadOnline(
    imageUrl: String
): ViewTarget<ImageView, Drawable> {

    return Glide.with(context).load(imageUrl).transition(withCrossFade()).thumbnail(0.5f).into(this)
}

fun View.fadeView(
    from: Float = 1.0f,
    to: Float = 0f,
    duration: Long = 1000,
) {
    val animation = AlphaAnimation(from, to)
    animation.duration = duration
    this.startAnimation(animation)

}

fun View.removeView() {
    visibility = View.GONE
}

fun View.showView() {
    visibility = View.VISIBLE
}

fun View.removeViewAnim() {
    fadeView()
    visibility = View.GONE
}

fun View.showViewAnim() {
    fadeView(from = 0f, to = 1f)
    visibility = View.VISIBLE
}