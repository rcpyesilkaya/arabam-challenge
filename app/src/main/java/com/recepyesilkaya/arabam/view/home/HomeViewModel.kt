package com.recepyesilkaya.arabam.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.data.network.RetrofitClient
import com.recepyesilkaya.arabam.data.paging.CarDataSource
import com.recepyesilkaya.arabam.data.paging.CarDataSourceFactory
import com.recepyesilkaya.arabam.util.State
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel : ViewModel() {

    private val apiService = RetrofitClient.getService()
    var cars: LiveData<PagedList<CarResponse>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 5
    private val carDataSourceFactory: CarDataSourceFactory

    val errorValue = MutableLiveData<Boolean>()
    val loadingValue = MutableLiveData<Boolean>()

    init {
        carDataSourceFactory = CarDataSourceFactory(compositeDisposable, apiService)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        cars = LivePagedListBuilder(carDataSourceFactory, config).build()
    }

    fun getState(): LiveData<State> =
        Transformations.switchMap(carDataSourceFactory.carsDataSourceLiveData, CarDataSource::state)

    fun retry() {
        carDataSourceFactory.carsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return cars.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun checkVisibility(state: State?) {
        loadingValue.value = listIsEmpty() && state == State.LOADING
        errorValue.value = listIsEmpty() && state == State.ERROR
        Log.e("JRDevRloadingValue", errorValue.value.toString())
        Log.e("JRDevRerrorValue", loadingValue.value.toString())
        Log.e("JRDevRX", state?.name.toString())
    }

}