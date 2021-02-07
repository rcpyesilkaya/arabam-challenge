package com.recepyesilkaya.arabam.data.mock

import android.content.Context
import com.recepyesilkaya.arabam.R
import com.recepyesilkaya.arabam.data.local.entity.SelectedCarEntity
import com.recepyesilkaya.arabam.data.model.*
import com.recepyesilkaya.arabam.ui.detail.advertdescription.DescriptionFragment
import com.recepyesilkaya.arabam.ui.detail.advertinfo.AdvertInfoFragment

object Mock {
    var selectedCars: ArrayList<SelectedCarEntity>? = arrayListOf(SelectedCarEntity(0))
    var selectedCarForBackId = 0
    var viewPagerAdvertInformations = ArrayList<ViewPagerAdvertInfo>()
    var viewPagerAdvertDescription = ""
    var requestSkip: Int = 10
    var advertSort: Sort = Sort("", "", "", null, null)
    var advertFilter: Filter = Filter(null, null, null, null, null)
    var sortTypes: ArrayList<Sort>? = null
    var filterYears: ArrayList<String>? = null
    var filterCategoryId: ArrayList<Int>? = null
    var filterCategoryName: ArrayList<String>? = null

    fun getListFragment(): List<FragmentModel> {
        val fragments = ArrayList<FragmentModel>()
        val advertInfoFragment = FragmentModel("İlan Bilgileri", AdvertInfoFragment())
        val descriptionFragment = FragmentModel("Açıklama", DescriptionFragment())
        fragments.add(advertInfoFragment)
        fragments.add(descriptionFragment)
        return fragments
    }

    fun carAdvertInfo(context: Context, carDetail: CarDetail) {
        val carAdvertInfo = ArrayList<ViewPagerAdvertInfo>()
        carAdvertInfo.add(
            ViewPagerAdvertInfo(
                context.getString(R.string.advert_price_title),
                carDetail.priceFormatted
            )
        )
        carAdvertInfo.add(
            ViewPagerAdvertInfo(
                context.getString(R.string.advert_no_title),
                carDetail.id.toString()
            )
        )
        carAdvertInfo.add(
            ViewPagerAdvertInfo(
                context.getString(R.string.advert_date_title),
                carDetail.dateFormatted
            )
        )
        carAdvertInfo.add(
            ViewPagerAdvertInfo(
                context.getString(R.string.advert_model_title),
                carDetail.modelName
            )
        )
        carAdvertInfo.add(
            ViewPagerAdvertInfo(
                context.getString(R.string.advert_year_title),
                carDetail.properties?.find { it.name == context.getString(R.string.advert_property_year_name) }?.value
            )
        )
        carAdvertInfo.add(
            ViewPagerAdvertInfo(
                context.getString(R.string.advert_fuel_title),
                carDetail.properties?.find { it.name == context.getString(R.string.advert_property_fuel_name) }?.value
            )
        )
        carAdvertInfo.add(
            ViewPagerAdvertInfo(
                context.getString(R.string.advert_gear_title),
                carDetail.properties?.find { it.name == context.getString(R.string.advert_property_gear_name) }?.value
            )
        )
        carAdvertInfo.add(
            ViewPagerAdvertInfo(
                context.getString(R.string.advert_km_title),
                carDetail.properties?.find { it.name == context.getString(R.string.advert_property_km_name) }?.value
            )
        )
        carAdvertInfo.add(
            ViewPagerAdvertInfo(
                context.getString(R.string.advert_color_title),
                carDetail.properties?.find { it.name == context.getString(R.string.advert_property_color_name) }?.value
            )
        )
        viewPagerAdvertInformations = carAdvertInfo
    }

    fun getSortList(context: Context): ArrayList<Sort> {
        sortTypes?.let {
            return it
        }
        val sortList = ArrayList<Sort>()
        sortList.add(
            Sort(
                context.getString(R.string.sort_type_price_desc_name),
                context.getString(R.string.sort_type_price_desc_message),
                context.getString(R.string.sort_type_price_desc_message),
                0,
                1
            )
        )
        sortList.add(
            Sort(
                context.getString(R.string.sort_type_price_asc_name),
                context.getString(R.string.sort_type_price_asc_message),
                context.getString(R.string.sort_type_price_asc_other_message),
                0,
                0
            )
        )
        sortList.add(
            Sort(
                context.getString(R.string.sort_type_date_desc_name),
                context.getString(R.string.sort_type_date_desc_message),
                context.getString(R.string.sort_type_date_desc_other_message),
                1,
                1
            )
        )
        sortList.add(
            Sort(
                context.getString(R.string.sort_type_date_asc_name),
                context.getString(R.string.sort_type_date_asc_message),
                context.getString(R.string.sort_type_date_asc_other_message),
                1,
                0
            )
        )
        sortList.add(
            Sort(
                context.getString(R.string.sort_type_year_desc_name),
                context.getString(R.string.sort_type_year_desc_message),
                context.getString(R.string.sort_type_year_desc_other_message),
                2,
                1
            )
        )
        sortList.add(
            Sort(
                context.getString(R.string.sort_type_year_asc_name),
                context.getString(R.string.sort_type_year_asc_message),
                context.getString(R.string.sort_type_year_asc_other_message),
                2,
                0
            )
        )
        sortTypes = sortList
        return sortList
    }

    fun getYearList(): ArrayList<String> {
        filterYears?.let {
            return it
        }
        val yearList = ArrayList<String>()
        yearList.add("Seçiniz..")
        for (year in 2021 downTo 1980) {
            yearList.add(year.toString())
        }
        filterYears = yearList
        return yearList
    }

    fun getCategoryList(context: Context) {
        filterCategoryId?.let {
            return
        }
        val categoryTempsId = ArrayList<Int>()
        categoryTempsId.add(0)
        categoryTempsId.add(82695)
        categoryTempsId.add(82144)
        categoryTempsId.add(69231)
        categoryTempsId.add(12094)
        categoryTempsId.add(23459)
        filterCategoryId = categoryTempsId

        val categoryTempsName = ArrayList<String>()
        categoryTempsName.add(context.getString(R.string.spinner_select))
        categoryTempsName.add(context.getString(R.string.category))
        categoryTempsName.add(context.getString(R.string.category1))
        categoryTempsName.add(context.getString(R.string.category2))
        categoryTempsName.add(context.getString(R.string.category3))
        categoryTempsName.add(context.getString(R.string.category4))
        filterCategoryName = categoryTempsName
    }
}