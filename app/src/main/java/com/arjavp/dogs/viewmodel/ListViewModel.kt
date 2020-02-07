package com.arjavp.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arjavp.dogs.model.DogBreed
import com.arjavp.dogs.model.DogsApiService
import com.arjavp.dogs.view.DogsListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel: ViewModel() {

    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable() //allows to avoid memory leaks while waiting for observable.

    //we need 3 livedata variables (3 views created in XML)
    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchFromRemote()
    }

    private fun fetchFromRemote(){
        loading.value=true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())// background thread
                .observeOn(AndroidSchedulers.mainThread()) //response processed on main thread
                .subscribeWith(object: DisposableSingleObserver<List<DogBreed>>(){

                    override fun onSuccess(dogList: List<DogBreed>) {
                        dogs.value = dogList
                        dogsLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}