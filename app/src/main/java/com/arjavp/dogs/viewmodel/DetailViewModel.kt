package com.arjavp.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arjavp.dogs.model.DogBreed

class DetailViewModel: ViewModel() {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(){
        val dog= DogBreed("1","Corgi","15","group", "bredFor","temperament","url")
        dogLiveData.value = dog
    }

}