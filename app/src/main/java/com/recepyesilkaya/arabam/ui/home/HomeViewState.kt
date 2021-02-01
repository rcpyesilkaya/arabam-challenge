package com.recepyesilkaya.arabam.ui.home

import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.util.toConvert

data class HomeViewState(private val car: CarResponse) {

    fun photo() = car.photo ?: ""

    fun priceFormatted() = car.priceFormatted ?: ""

    fun categoryName() = car.category?.name ?: ""

    fun title() = car.title ?: ""

    fun location() = "${car.location?.cityName} / ${car.location?.townName}"

    fun dateFormatted() = car.dateFormatted ?: ""

    fun properties(): String {
        return car.properties.toConvert()
    }
}