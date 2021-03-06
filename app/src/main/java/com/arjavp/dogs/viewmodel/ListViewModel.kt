package com.arjavp.dogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arjavp.dogs.model.DogBreed
import com.arjavp.dogs.model.DogDatabase
import com.arjavp.dogs.model.DogsApiService
import com.arjavp.dogs.util.NotificationsHelper
import com.arjavp.dogs.util.SharedPreferencesHelper
import com.arjavp.dogs.view.DogsListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class ListViewModel(application: Application): BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5*60*1000*1000*1000L //5 mins in nano seconds.

    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable() //allows to avoid memory leaks while waiting for observable.

    //we need 3 livedata variables (3 views created in XML)
    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if(updateTime !=null && updateTime!=0L && System.nanoTime()-updateTime< refreshTime){
            fetchFromDatabase()
        }else{
            fetchFromRemote()
        }
    }

    private fun checkCacheDuration(){
        val cachePreference = prefHelper.getCacheduration()

        try{
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000*1000*1000L)
        }catch(e: NumberFormatException){
            //exception when we convert String to Int and it's not an Int
            e.printStackTrace()
        }
    }

    fun refreshBypassCache(){
        fetchFromRemote()
    }

    private fun fetchFromDatabase(){
        loading.value = true
        //needs to be on background thread
        launch{
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(), "Dogs retrieved from database", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFromRemote(){
        loading.value=true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())// background thread
                .observeOn(AndroidSchedulers.mainThread()) //response processed on main thread
                .subscribeWith(object: DisposableSingleObserver<List<DogBreed>>(){

                    override fun onSuccess(dogList: List<DogBreed>) {
                        //when we retrieve info from endpoint, we will store it in db and then update UI.
                        storeDogsLocally(dogList)
                        Toast.makeText(getApplication(), "Dogs retrieved from endpoint", Toast.LENGTH_SHORT).show()
                        NotificationsHelper(getApplication()).createNotification()//creates notif when we retrieve info from backend.
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun dogsRetrieved(dogList: List<DogBreed>){
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun storeDogsLocally(list: List<DogBreed>){
        launch{
            //this will be run as a coroutine on seperate thread.
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAlldogs()
            val result = dao.insertAll(*list.toTypedArray())//gets  list and expands it into individual elements
            //elements passed into insertAll fun will give us UUids.
            //to assign Uuids to dog objects.
            var i=0
            while(i<list.size){
                list[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}