package com.recepyesilkaya.arabam.util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.downloadImageFromUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .into(this)
}
