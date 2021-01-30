package com.recepyesilkaya.arabam.data.repository

import com.recepyesilkaya.arabam.data.network.APIService

class CarRepository(private val apiService: APIService) {

    fun getCarData(sort: Int, sortDirection: Int, take: Int) =
        apiService.getData(sort, sortDirection, take)

    fun getDetail(id: Long) = apiService.getDetail(id)

}