package com.recepyesilkaya.arabam.view.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denzcoskun.imageslider.models.SlideModel
import com.recepyesilkaya.arabam.data.local.dao.CarEntityDAO
import com.recepyesilkaya.arabam.data.local.database.CarRoomDatabase
import com.recepyesilkaya.arabam.data.model.CarDetail
import com.recepyesilkaya.arabam.data.network.RetrofitClient
import com.recepyesilkaya.arabam.data.repository.CarRepository
import com.recepyesilkaya.arabam.util.Resource
import com.recepyesilkaya.arabam.util.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailViewModel(application: Application) : AndroidViewModel(application) {


    private val compositeDisposable = CompositeDisposable()
    private val apiService = RetrofitClient.getService()

    private val carRepository: CarRepository
    val carEntityDAO: CarEntityDAO = CarRoomDatabase.getDatabase(application).carEntityDAO()

    init {
        carRepository = CarRepository(apiService, carEntityDAO)
    }

    lateinit var imageSize: String
    private var imageList = ArrayList<SlideModel>()

    private val _images = MutableLiveData<ArrayList<SlideModel>>()
    val images: LiveData<ArrayList<SlideModel>>
        get() = _images

    var carDetailResource = MutableLiveData<Resource<CarDetail>>()

    fun getCarDetail(id: Long) {
        carDetailResource.postValue(Resource(state = State.LOADING, data = null, message = ""))

        compositeDisposable.add(
            carRepository.getDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        response?.let {
                            carDetailResource.postValue(
                                Resource(
                                    state = State.SUCCESS,
                                    data = it,
                                    message = null
                                )
                            )

                        }
                    },
                    { error ->
                        carDetailResource.postValue(
                            Resource(
                                state = State.ERROR,
                                data = null,
                                message = error.message
                            )
                        )
                    }
                )
        )
    }

    fun bindImages(t: CarDetail) {
        imageList.clear()
        t.photos?.let {
            it.forEach { url ->
                val urlChange = url.replace("{0}", imageSize)
                imageList.add(SlideModel(urlChange))
            }
        }
        _images.value = imageList
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}