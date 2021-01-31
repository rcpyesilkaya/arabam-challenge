package com.recepyesilkaya.arabam.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("loadImage", "imageSize")
fun ImageView.setImage(url: String?, imageSize: String) {
    if (!url.isNullOrEmpty()) {
        val urlChange = url.replace("{0}", imageSize)
        this.downloadImageFromUrl(urlChange)
    }
}

@BindingAdapter("toHtml")
fun TextView?.toHtml(description: String) {
    if (description.isNotEmpty()) {
        this?.text = description.toHtml()
    }
}

