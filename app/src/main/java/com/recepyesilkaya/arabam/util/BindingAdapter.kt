package com.recepyesilkaya.arabam.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("loadImage", "imageSize")
fun ImageView.setImage(url: String?, imageSize: String) {
    if (!url.isNullOrEmpty()) {
        val urlChange = url.replace("{0}", imageSize)
        this.downloadImageFromUrl(urlChange)
    }
}
/*
@BindingAdapter("loadImage", "imageSize")
fun ImageSlider.bindImage(urls: List<String?>?, imageSize: String) {
    val imageList = ArrayList<SlideModel>()
    if (!urls.isNullOrEmpty()) {
        urls.forEach {
            val urlChange= it?.replace("{0}", imageSize)
            urlChange?.let{
                imageList.add(SlideModel(urlChange))
            }
        }

    }

    this.setImageList(imageList)
}*/

