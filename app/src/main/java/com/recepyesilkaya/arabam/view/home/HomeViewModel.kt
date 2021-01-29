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


    /*
    private val compositeDisposable = CompositeDisposable()
    private val apiService = RetrofitClient.getRetrofit().create(APIService::class.java)
    private val carRepository = CarRepository(apiService)

    private var _carList = MutableLiveData<List<CarResponse>>()
    val carList: LiveData<List<CarResponse>>
        get() = _carList

    private val _loadingValue = MutableLiveData<Boolean>()
    val loadingValue: LiveData<Boolean>
        get() = _loadingValue

    private val _successValue = MutableLiveData<Boolean>()
    val successValue: LiveData<Boolean>
        get() = _successValue

    private val _errorValue = MutableLiveData<Boolean>()
    val errorValue: LiveData<Boolean>
        get() = _errorValue

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun getCarData(sort: Int, sortDirection: Int, take: Int) {
        resourceStateData(Resource(State.LOADING, data = null, message = ""))

        //val getControlData = carRepository.getControlData()

        // if (getControlData.isNullOrEmpty() || !statusDataLocal) {
        compositeDisposable.add(
            carRepository.getCarData(sort, sortDirection, take)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<CarResponse>>() {
                    override fun onSuccess(t: List<CarResponse>) {
                        // carRepository.insertData(t)
                        resourceStateData(Resource(State.SUCCESS, data = t, message = ""))
                    }

                    override fun onError(e: Throwable) {
                        resourceStateData(Resource(State.ERROR, data = null, message = ""))
                    }
                })
        )
        /*} else {
            _carList.postValue(Resource(State.SUCCESS, data = getControlData, message = ""))
            Log.e("dataTipi","Yerel ")
        }*/
    }


    private fun resourceStateData(resource: Resource<List<CarResponse>>) {
        when (resource.state) {
            State.LOADING -> {
                Log.e("Data", "Loading")
                _loadingValue.value = true
                _successValue.value = false
                _errorValue.value = false
            }
            State.SUCCESS -> {
                Log.e("Data", "Success")
                resource.data?.let { _carList.value = it }
                _loadingValue.value = false
                _successValue.value = true
                _errorValue.value = false
            }
            State.ERROR -> {
                resource.message
                Log.e("Data", "Error")
                _error.value = "Veriler Yüklenirken Hata Oluştu! ${resource.message} "
                _loadingValue.value = false
                _successValue.value = false
                _errorValue.value = true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }*/
}