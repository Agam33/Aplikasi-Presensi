package com.orb.myapplication.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.orb.myapplication.R

fun ImageView.loadImage(url: String?) {
    Glide
        .with(this.context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.ic_launcher_background)
        .centerCrop()
        .into(this)
}

fun View.hideView(state: Boolean) {
    if(state) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

/*
    author: Riswan Agam
    email: riswanagam@gmail.com
 */