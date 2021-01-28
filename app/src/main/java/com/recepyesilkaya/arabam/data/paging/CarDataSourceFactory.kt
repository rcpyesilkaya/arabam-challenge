package com.recepyesilkaya.arabam.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.data.network.APIService
import io.reactivex.disposables.CompositeDisposable

class CarDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val apiService: APIService
) : DataSource.Factory<Int, CarResponse>() {

    val carsDataSourceLiveData = MutableLiveData<CarDataSource>()

    override fun create(): DataSource<Int, CarResponse> {
        val carsDataSource = CarDataSource(apiService, compositeDisposable)
        carsDataSourceLiveData.postValue(carsDataSource)
        return carsDataSource
    }
}