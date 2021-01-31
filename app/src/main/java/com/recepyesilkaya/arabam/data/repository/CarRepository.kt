package com.recepyesilkaya.arabam.data.repository

import androidx.lifecycle.LiveData
import com.recepyesilkaya.arabam.data.local.dao.CarEntityDAO
import com.recepyesilkaya.arabam.data.local.entity.CarEntity
import com.recepyesilkaya.arabam.data.network.APIService

class CarRepository(
    private val apiService: APIService,
    private val carEntityDAO: CarEntityDAO
) {

    fun getCarData(sort: Int, sortDirection: Int, take: Int) =
        apiService.getData(20, take)

    fun getDetail(id: Long) = apiService.getDetail(id)

    fun getAllCars(): LiveData<List<CarEntity>> = carEntityDAO.getAllCars()

    fun insert(carEntity: CarEntity) = carEntityDAO.insert(carEntity)

    fun deleteAll() = carEntityDAO.deleteAll()


}