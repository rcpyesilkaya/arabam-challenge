package com.recepyesilkaya.arabam.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.data.network.APIService
import com.recepyesilkaya.arabam.data.network.RetrofitClient
import com.recepyesilkaya.arabam.data.repository.CarRepository
import com.recepyesilkaya.arabam.util.Resource
import com.recepyesilkaya.arabam.util.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {

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
        resourceStatusData(Resource(Status.LOADING, data = null, message = ""))

        //val getControlData = carRepository.getControlData()

        // if (getControlData.isNullOrEmpty() || !statusDataLocal) {
        compositeDisposable.add(
            carRepository.getCarData(sort, sortDirection, take)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<CarResponse>>() {
                    override fun onSuccess(t: List<CarResponse>) {
                        // carRepository.insertData(t)
                        resourceStatusData(Resource(Status.SUCCESS, data = t, message = ""))
                    }

                    override fun onError(e: Throwable) {
                        resourceStatusData(Resource(Status.ERROR, data = null, message = ""))
                    }
                })
        )
        /*} else {
            _carList.postValue(Resource(Status.SUCCESS, data = getControlData, message = ""))
            Log.e("dataTipi","Yerel ")
        }*/
    }


    private fun resourceStatusData(resource: Resource<List<CarResponse>>) {
        when (resource.status) {
            Status.LOADING -> {
                Log.e("Data", "Loading")
                _loadingValue.value = true
                _successValue.value = false
                _errorValue.value = false
            }
            Status.SUCCESS -> {
                Log.e("Data", "Success")
                resource.data?.let { _carList.value = it }
                _loadingValue.value = false
                _successValue.value = true
                _errorValue.value = false
            }
            Status.ERROR -> {
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
    }
}