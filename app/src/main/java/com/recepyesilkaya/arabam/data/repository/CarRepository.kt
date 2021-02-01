package com.recepyesilkaya.arabam.data.repository

import androidx.lifecycle.LiveData
import com.recepyesilkaya.arabam.data.local.dao.CarDAO
import com.recepyesilkaya.arabam.data.local.entity.CarEntity
import com.recepyesilkaya.arabam.data.network.APIService
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val apiService: APIService,
    private val carDAO: CarDAO
) {

    fun getCarData(sort: Int, sortDirection: Int, take: Int) =
        apiService.getData(20, take)

    fun getDetail(id: Long) = apiService.getDetail(id)

    fun getAllCars(): LiveData<List<CarEntity>> = carDAO.getAllCars()

    fun insert(carEntity: CarEntity) = carDAO.insert(carEntity)

    fun deleteAll() = carDAO.deleteAll()
}