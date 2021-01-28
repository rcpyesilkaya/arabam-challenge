package com.recepyesilkaya.arabam.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.recepyesilkaya.arabam.data.model.CarResponse
import com.recepyesilkaya.arabam.data.network.APIService
import com.recepyesilkaya.arabam.util.State
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers


class CarDataSource(
    private val apiService: APIService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, CarResponse>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, CarResponse>
    ) {
        updateState(State.LOADING)
        compositeDisposable.add(
            apiService.getData(1, 0, params.requestedLoadSize)
                .subscribe(
                    { response ->
                        updateState(State.SUCCESS)
                        callback.onResult(
                            response,
                            null,
                            2
                        )
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, CarResponse>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, CarResponse>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            apiService.getData(params.key, 0, params.requestedLoadSize)
                .subscribe(
                    { response ->
                        updateState(State.SUCCESS)
                        callback.onResult(
                            response,
                            params.key + 1
                        )
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }
}