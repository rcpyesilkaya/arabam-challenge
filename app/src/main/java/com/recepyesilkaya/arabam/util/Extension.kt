package com.recepyesilkaya.arabam.util

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.local.entity.CarEntity
import com.recepyesilkaya.arabam.data.local.entity.SelectedCarEntity
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.data.model.Property

fun ImageView.downloadImageFromUrl(url: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.error_image)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}


fun getPlaceHolder(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
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

fun List<Property>?.toConvert(): String {
    var result: String = ""
    this?.forEach { property ->

        if (property.name == "year") result += "${property.value}"
    }
    this?.forEach { property ->
        when (property.name) {
            "km" -> result += " ‧ ${property.value} km"
            "color" -> result += " ‧ ${property.value} "
        }
    }
    return result
}

fun List<SelectedCarEntity>.toArray(): ArrayList<SelectedCarEntity> {
    val temps = ArrayList<SelectedCarEntity>()
    this.forEach {
        temps.add(it)
    }
    return temps
}



