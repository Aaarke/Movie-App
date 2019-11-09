package com.example.mvvmbaseproject.ui.main

import RestClient
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.mvvmbaseproject.model.MovieDataPro
import com.example.mvvmbaseproject.ui.base.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel : BaseViewModel<Any>() {
    var data = MutableLiveData<MovieDataPro>()
    var dataLoadError = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
    val compositeDisposable = CompositeDisposable()

    /**
     ************************* Making api call to the server using rx java and retrofit ************
     * */
    fun fetchData(context: Context, searchKey: String) {
        loading.value = true
        val map = HashMap<String, String>()
        map["s"] = searchKey
        map["i"] = "tt3896198"
        map["apikey"] = "81117353"
        compositeDisposable.add(
            RestClient.getApiInterface(context).movieDataFromServer(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieDataPro>() {
                    override fun onSuccess(movieData: MovieDataPro) {
                        data.value = movieData
                        dataLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(throwable: Throwable) {
                        dataLoadError.value = true
                        loading.value = false
                        throwable.printStackTrace()
                    }

                }
                )
        )

    }

}
