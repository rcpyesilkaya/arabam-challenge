package com.recepyesilkaya.arabam.util

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.recepyesilkaya.arabam.data.local.entity.CarEntity
import com.recepyesilkaya.arabam.data.model.CarResponse

fun ImageView.downloadImageFromUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .into(this)
}

fun String?.toHtml(): Spanned? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(
            this,
            Html.FROM_HTML_MODE_COMPACT
        )
    } else {
        Html.fromHtml(this)
    }
}

fun CarResponse.toEntity(): CarEntity {
    return CarEntity(
        id,
        title,
        location?.cityName,
        location?.townName,
        category?.id,
        category?.name,
        modelName,
        price,
        priceFormatted,
        date,
        dateFormatted
    )
}




