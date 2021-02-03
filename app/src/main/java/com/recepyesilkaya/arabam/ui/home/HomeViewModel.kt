package com.recepyesilkaya.arabam.ui.home

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.recepyesilkaya.arabam.data.local.entity.SelectedCarEntity
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.data.paging.CarDataSource
import com.recepyesilkaya.arabam.data.paging.CarDataSourceFactory
import com.recepyesilkaya.arabam.data.repository.CarRepository
import com.recepyesilkaya.arabam.util.State
import com.recepyesilkaya.arabam.util.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val carRepository: CarRepository,
    private val carDataSourceFactory: CarDataSourceFactory,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    val errorValue = MutableLiveData<Boolean>()
    val loadingValue = MutableLiveData<Boolean>()

    var cars: LiveData<PagedList<CarResponse>>
    private val pageSize = 10
    var firstTime = true

    private val _selectedCars = MutableLiveData<List<SelectedCarEntity>>()
    val selectedCars: LiveData<List<SelectedCarEntity>>
        get() = _selectedCars

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        cars = LivePagedListBuilder(carDataSourceFactory, config).build()

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

    fun getSelectedCars() {
        viewModelScope.launch {
            _selectedCars.postValue(carRepository.getAllSelectedCars())
        }
    }

}