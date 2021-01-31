package com.recepyesilkaya.arabam.view.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.recepyesilkaya.arabam.data.local.dao.CarEntityDAO
import com.recepyesilkaya.arabam.data.local.database.CarRoomDatabase
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.data.network.APIService
import com.recepyesilkaya.arabam.data.network.RetrofitClient
import com.recepyesilkaya.arabam.data.paging.CarDataSource
import com.recepyesilkaya.arabam.data.paging.CarDataSourceFactory
import com.recepyesilkaya.arabam.data.repository.CarRepository
import com.recepyesilkaya.arabam.util.State
import com.recepyesilkaya.arabam.util.toEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val carRepository: CarRepository
    // val localCars: LiveData<List<CarEntity>>

    val errorValue = MutableLiveData<Boolean>()
    val loadingValue = MutableLiveData<Boolean>()

    //-------------------------------------------------------

    private var apiService: APIService = RetrofitClient.getService()
    var cars: LiveData<PagedList<CarResponse>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 10
    private val carDataSourceFactory: CarDataSourceFactory

    init {
        carDataSourceFactory = CarDataSourceFactory(compositeDisposable, apiService)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        cars = LivePagedListBuilder(carDataSourceFactory, config).build()

        //------------------------------------------------------------------------------------------

        val carEntityDAO: CarEntityDAO = CarRoomDatabase.getDatabase(application).carEntityDAO()

        carRepository =
            CarRepository(apiService, carEntityDAO)
        // localCars = carRepository.getAllCars()

    }

    fun getState(): LiveData<State> =
        Transformations.switchMap<CarDataSource,
                State>(carDataSourceFactory.carsDataSourceLiveData, CarDataSource::state)

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

    fun carsAddDatabase(carResponse: List<CarResponse>) {
        carResponse.forEach {
            compositeDisposable.add(
                carRepository.insert(it.toEntity())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

}