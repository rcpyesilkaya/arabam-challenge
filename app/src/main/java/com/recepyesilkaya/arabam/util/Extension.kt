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
import com.recepyesilkaya.arabam.data.local.entity.FavoriteEntity
import com.recepyesilkaya.arabam.data.model.*

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

fun FavoriteEntity.toCarResponse(): CarResponse {
    return CarResponse(
        this.car_id,
        this.title,
        CarLocation(this.cityName, this.townName),
        Category(this.categoryId, this.categoryName),
        this.modelName,
        this.price,
        this.priceFormatted,
        this.date,
        this.dateFormatted,
        this.photo,
        null
    )
}

fun CarDetail.toFavorite(): FavoriteEntity {
    return FavoriteEntity(
        this.id!!.toLong(),
        this.title,
        this.location?.cityName,
        this.location?.townName,
        this.category?.id,
        this.category?.name,
        this.modelName,
        this.price,
        this.priceFormatted,
        this.date,
        this.dateFormatted,
        this.photos?.firstOrNull()
    )
}



