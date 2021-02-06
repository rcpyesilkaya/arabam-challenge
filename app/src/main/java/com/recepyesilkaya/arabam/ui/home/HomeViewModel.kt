package com.recepyesilkaya.arabam.ui.home

import android.content.Context
import android.view.View
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.recepyesilkaya.arabam.data.local.entity.SelectedCarEntity
import com.recepyesilkaya.arabam.data.mock.Mock
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.data.model.Sort
import com.recepyesilkaya.arabam.data.paging.CarDataSource
import com.recepyesilkaya.arabam.data.paging.CarDataSourceFactory
import com.recepyesilkaya.arabam.data.repository.CarRepository
import com.recepyesilkaya.arabam.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val carRepository: CarRepository,
    private val carDataSourceFactory: CarDataSourceFactory,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    val errorValue = MutableLiveData<Boolean>()
    val loadingValue = MutableLiveData<Boolean>()
    val filterErrorValue = MutableLiveData<Boolean>()
    val filterSuccessValue = MutableLiveData<Boolean>()
    val isStyleChange = MutableLiveData<Boolean>()


    var cars: LiveData<PagedList<CarResponse>>
    private val pageSize = 10
    var firstTime = true

    private val _selectedCars = MutableLiveData<List<SelectedCarEntity>>()
    val selectedCars: LiveData<List<SelectedCarEntity>>
        get() = _selectedCars

    private val _sort = MutableLiveData<Sort?>()
    val sort: LiveData<Sort?>
        get() = _sort

    private val _isMenuOpen = MutableLiveData<Boolean>()
    val isMenuOpen: LiveData<Boolean>
        get() = _isMenuOpen

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        cars = LivePagedListBuilder(carDataSourceFactory, config).build()
        isStyleChange.value = false

        viewModelScope.launch {
            _selectedCars.postValue(carRepository.getAllSelectedCars())
        }
        _isMenuOpen.value = false
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

    fun checkVisibility(state: State?) {
        loadingValue.value = listIsEmpty() && state == State.LOADING
        errorValue.value = listIsEmpty() && state == State.ERROR
    }

    fun sortResult(sort: Sort?, context: Context, message: String?) {
        var result: Sort? = null
        message?.let { message ->
            result = Mock.getSortList(context)
                .find { it.sortMessage == message || it.sortMessageOther == message }
        }

        sort?.let { sort ->
            result = Mock.getSortList(context).find { it.sortName == sort.sortName }
        }

        result?.let {
            Mock.advertSort = it
        }

        _sort.value = result
    }

    fun onClickFab(view: View) {
        _isMenuOpen.value = !_isMenuOpen.value!!
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}