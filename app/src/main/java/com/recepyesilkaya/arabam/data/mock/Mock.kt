package com.recepyesilkaya.arabam.data.mock

import com.recepyesilkaya.arabam.data.model.CarAdvertInfo
import com.recepyesilkaya.arabam.data.model.FragmentModel
import com.recepyesilkaya.arabam.view.detail.advertinfo.AdvertInfoFragment
import com.recepyesilkaya.arabam.view.detail.description.DescriptionFragment

object Mock {
    fun getListFragment(): List<FragmentModel> {
        val fragments = ArrayList<FragmentModel>()
        val advertInfoFragment = FragmentModel("İlan Bilgileri", AdvertInfoFragment())
        val descriptionFragment = FragmentModel("Açıklama", DescriptionFragment())
        fragments.add(advertInfoFragment)
        fragments.add(descriptionFragment)
        return fragments
    }

    var carAdverts = ArrayList<CarAdvertInfo>()
    var description = ""

}