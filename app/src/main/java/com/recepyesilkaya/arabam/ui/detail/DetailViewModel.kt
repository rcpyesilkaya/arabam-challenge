package com.recepyesilkaya.arabam.ui.detail

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recepyesilkaya.arabam.data.local.entity.SelectedCarEntity
import com.recepyesilkaya.arabam.data.model.CarDetail
import com.recepyesilkaya.arabam.data.model.User
import com.recepyesilkaya.arabam.data.repository.CarRepository
import com.recepyesilkaya.arabam.util.Resource
import com.recepyesilkaya.arabam.util.State
import com.recepyesilkaya.arabam.util.toFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val carRepository: CarRepository) : ViewModel() {

    lateinit var imageSize: String
    private var imageList = ArrayList<CarouselItem>()
    private val compositeDisposable = CompositeDisposable()

    val carDetail = MutableLiveData<CarDetail>()
    val userInfo = MutableLiveData<User>()
    val carShareInfo = MutableLiveData<String>()
    val carDetailResource = MutableLiveData<Resource<CarDetail>>()
    val isFavorite = MutableLiveData<Boolean>()
    var selectionCarId: Long? = null

    lateinit var onBackClick: () -> Unit
    lateinit var startIntentPhone: (Intent) -> Unit
    lateinit var startIntentShareCar: (Intent) -> Unit

    private val _images = MutableLiveData<ArrayList<CarouselItem>>()
    val images: LiveData<ArrayList<CarouselItem>>
        get() = _images

    init {
        getFavorites()
    }

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
                            carDetail.value = it
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

    private fun getFavorites() {
        viewModelScope.launch {
            val favorite = carRepository.getAllFavorite().find { it.car_id == selectionCarId }
            isFavorite.value = favorite != null
        }
    }

    fun onClickFavorite(view: View) {
        if (isFavorite.value!!) {
            viewModelScope.launch {
                carRepository.deleteFavorite(selectionCarId!!)
                isFavorite.value = false
            }
        } else {
            viewModelScope.launch {
                val favorite = carDetailResource.value?.data?.toFavorite()
                favorite?.let {
                    carRepository.insertFavorite(it)
                    isFavorite.value = true
                }
            }
        }
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

    fun bindImages(carDetail: CarDetail) {
        imageList.clear()
        carDetail.photos?.let {
            it.forEach { url ->
                val urlChange = url.replace("{0}", imageSize)
                imageList.add(CarouselItem(urlChange))
            }
        }
        _images.value = imageList
    }

    fun onClickBack(onBackClick: () -> Unit) {
        this.onBackClick = onBackClick
    }

    fun onClickPhone(view: View) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${carDetail.value?.userInfo?.phone}")
        startIntentPhone.invoke(intent)
    }

    fun onClickWhatsApp(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.setPackage("com.whatsapp")
        intent.putExtra(Intent.EXTRA_TEXT, carShareInfo.value)
        startIntentShareCar.invoke(intent)
    }

    fun onClickShare(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, carShareInfo.value)
        startIntentShareCar.invoke(intent)
    }

    fun bindHigherOrderPhone(startIntentPhone: (Intent) -> Unit) {
        this.startIntentPhone = startIntentPhone
    }

    fun bindHigherOrderShareCar(startIntentShareCar: (Intent) -> Unit) {
        this.startIntentShareCar = startIntentShareCar
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}