package com.recepyesilkaya.arabam.ui.detail

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.denzcoskun.imageslider.models.SlideModel
import com.recepyesilkaya.arabam.data.local.entity.SelectedCarEntity
import com.recepyesilkaya.arabam.data.model.CarDetail
import com.recepyesilkaya.arabam.data.repository.CarRepository
import com.recepyesilkaya.arabam.util.Resource
import com.recepyesilkaya.arabam.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val carRepository: CarRepository) : ViewModel() {

    lateinit var imageSize: String
    private var imageList = ArrayList<SlideModel>()
    lateinit var onBackClick: () -> Unit

    private val compositeDisposable = CompositeDisposable()
    var carDetailResource = MutableLiveData<Resource<CarDetail>>()

    private val _images = MutableLiveData<ArrayList<SlideModel>>()
    val images: LiveData<ArrayList<SlideModel>>
        get() = _images

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    var carDetail: CarDetail? = null

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
                            carDetail = it
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

    fun addSelectedCar(id: Int) {
        val selectedCarEntity = SelectedCarEntity(id)

        compositeDisposable.add(
            carRepository.addSelectCar(selectedCarEntity)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
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

    fun onClickBack(onBackClick: () -> Unit) {
        this.onBackClick = onBackClick
    }

    fun onClickWhatsApp(view: View) {
        _message.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}