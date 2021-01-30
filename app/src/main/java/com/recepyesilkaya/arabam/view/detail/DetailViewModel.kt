package com.recepyesilkaya.arabam.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.denzcoskun.imageslider.models.SlideModel
import com.recepyesilkaya.arabam.data.model.CarAdvertInfo
import com.recepyesilkaya.arabam.data.model.CarDetail
import com.recepyesilkaya.arabam.data.network.RetrofitClient
import com.recepyesilkaya.arabam.data.repository.CarRepository
import com.recepyesilkaya.arabam.util.Resource
import com.recepyesilkaya.arabam.util.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class DetailViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val apiService = RetrofitClient.getService()
    private val carRepository = CarRepository(apiService)

    lateinit var imageSize: String
    private var imageList = ArrayList<SlideModel>()
    var carAdverts = ArrayList<CarAdvertInfo>()

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
                .subscribeWith(object : DisposableSingleObserver<CarDetail?>() {
                    override fun onSuccess(t: CarDetail) {
                        carDetailResource.postValue(
                            Resource(
                                state = State.SUCCESS,
                                data = t,
                                message = null
                            )
                        )
                        bindImages(t)
                    }

                    override fun onError(e: Throwable) {
                        carDetailResource.postValue(
                            Resource(
                                state = State.ERROR,
                                data = null,
                                message = e.message
                            )
                        )
                    }
                })
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