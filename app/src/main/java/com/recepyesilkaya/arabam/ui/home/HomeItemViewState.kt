package com.recepyesilkaya.arabam.ui.home

import com.recepyesilkaya.arabam.data.local.entity.SelectedCarEntity
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.util.toConvert

data class HomeItemViewState(
    private val car: CarResponse,
    private val selectedCars: List<SelectedCarEntity>?
) {

    fun selectedCarsFind(): Boolean? {
        val find = selectedCars?.find { it.selected_car_id == car.id.toInt() }

        find?.let {
            return true
        }
        return false
    }

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