package com.recepyesilkaya.arabam.data.repository

import com.recepyesilkaya.arabam.data.local.dao.CarDAO
import com.recepyesilkaya.arabam.data.local.dao.SelectedCarDAO
import com.recepyesilkaya.arabam.data.local.entity.SelectedCarEntity
import com.recepyesilkaya.arabam.data.network.APIService
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val apiService: APIService,
    private val carDAO: CarDAO,
    private val selectedCarDAO: SelectedCarDAO
) {
    fun getDetail(id: Long) = apiService.getDetail(id)

    fun addSelectCar(selectedCarEntity: SelectedCarEntity) =
        selectedCarDAO.addSelectCar(selectedCarEntity)

    suspend fun getAllSelectedCars() = selectedCarDAO.getAllSelectedCars()
}