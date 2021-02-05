package com.recepyesilkaya.arabam.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.data.network.APIService
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CarDataSourceFactory @Inject constructor(
    private val compositeDisposable: CompositeDisposable,
    private val apiService: APIService,
    private val carDataSource: CarDataSource
) : DataSource.Factory<Int, CarResponse>() {

    val carsDataSourceLiveData = MutableLiveData<CarDataSource>()
    override fun create(): DataSource<Int, CarResponse> {
        carsDataSourceLiveData.postValue(carDataSource)
        return carDataSource
    }
}