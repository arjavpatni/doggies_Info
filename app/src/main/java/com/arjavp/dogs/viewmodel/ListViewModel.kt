package com.arjavp.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arjavp.dogs.model.DogBreed
import com.arjavp.dogs.view.DogsListAdapter

class ListViewModel: ViewModel() {
    //we need 3 livedata variables
    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        val dog1= DogBreed("1","Corgi","15","group", "bredFor","url")
        val dog2= DogBreed("1","Labrador","10","group", "bredFor","url")
        val dog3= DogBreed("1","Rottweiler","20","group", "bredFor","url")
        val dogList = arrayListOf<DogBreed>(dog1, dog2, dog3)

        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }
}