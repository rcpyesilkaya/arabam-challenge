package com.recepyesilkaya.arabam.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recepyesilkaya.arabam.data.local.entity.FavoriteEntity
import com.recepyesilkaya.arabam.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _favorites = MutableLiveData<List<FavoriteEntity>>()
    val favorites: LiveData<List<FavoriteEntity>>
        get() = _favorites

    val swipeState = MutableLiveData<Boolean>()
    val isFavoritesEmpty = MutableLiveData<Boolean>()

    lateinit var onBackClick: () -> Unit

    init {
        getFavorites()
        swipeState.value = true
    }

    fun getFavorites() {
        viewModelScope.launch {

            val result = carRepository.getAllFavorite()
            isFavoritesEmpty.value = result.isEmpty()
            _favorites.value = result
        }
    }

    fun onClickBack(onBackClick: () -> Unit) {
        this.onBackClick = onBackClick
    }


}