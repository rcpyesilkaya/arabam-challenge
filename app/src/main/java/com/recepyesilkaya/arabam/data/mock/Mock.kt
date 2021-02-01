package com.recepyesilkaya.arabam.data.mock

import android.content.Context
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.model.CarAdvertInfo
import com.recepyesilkaya.arabam.data.model.CarDetail
import com.recepyesilkaya.arabam.data.model.FragmentModel
import com.recepyesilkaya.arabam.ui.detail.advertinfo.AdvertInfoFragment
import com.recepyesilkaya.arabam.ui.detail.description.DescriptionFragment

object Mock {
    var carAdverts = ArrayList<CarAdvertInfo>()
    var description = ""

    fun getListFragment(): List<FragmentModel> {
        val fragments = ArrayList<FragmentModel>()
        val advertInfoFragment = FragmentModel("İlan Bilgileri", AdvertInfoFragment())
        val descriptionFragment = FragmentModel("Açıklama", DescriptionFragment())
        fragments.add(advertInfoFragment)
        fragments.add(descriptionFragment)
        return fragments
    }

    fun carAdvertInfo(context: Context, carDetail: CarDetail) {
        val carAdvertInfo = ArrayList<CarAdvertInfo>()
        carAdvertInfo.add(
            CarAdvertInfo(
                context.getString(R.string.advert_price_title),
                carDetail.priceFormatted
            )
        )
        carAdvertInfo.add(
            CarAdvertInfo(
                context.getString(R.string.advert_no_title),
                carDetail.id.toString()
            )
        )
        carAdvertInfo.add(
            CarAdvertInfo(
                context.getString(R.string.advert_date_title),
                carDetail.dateFormatted
            )
        )
        carAdvertInfo.add(
            CarAdvertInfo(
                context.getString(R.string.advert_model_title),
                carDetail.modelName
            )
        )
        carAdvertInfo.add(
            CarAdvertInfo(
                context.getString(R.string.advert_year_title),
                carDetail.properties?.find { it.name == context.getString(R.string.advert_property_year_name) }?.value
            )
        )
        carAdvertInfo.add(
            CarAdvertInfo(
                context.getString(R.string.advert_fuel_title),
                carDetail.properties?.find { it.name == context.getString(R.string.advert_property_fuel_name) }?.value
            )
        )
        carAdvertInfo.add(
            CarAdvertInfo(
                context.getString(R.string.advert_gear_title),
                carDetail.properties?.find { it.name == context.getString(R.string.advert_property_gear_name) }?.value
            )
        )
        carAdvertInfo.add(
            CarAdvertInfo(
                context.getString(R.string.advert_km_title),
                carDetail.properties?.find { it.name == context.getString(R.string.advert_property_km_name) }?.value
            )
        )
        carAdvertInfo.add(
            CarAdvertInfo(
                context.getString(R.string.advert_color_title),
                carDetail.properties?.find { it.name == context.getString(R.string.advert_property_color_name) }?.value
            )
        )
        carAdverts = carAdvertInfo
    }

    var skip: Int = 10
}