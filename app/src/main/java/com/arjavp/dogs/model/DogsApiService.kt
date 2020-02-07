package com.arjavp.dogs.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DogsApiService {

    private val BASE_URL = "https://raw.githubusercontent.com"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(DogsApi::class.java)
    //here, api is an object created by retrofit to call the endpoint and retrieve info.
    //GsonConverterFactory converts JSON into model(class) created by us.
    //RxJava2CallAdapterFactory converts list of our elements into observable(a Single).
    fun getDogs(): Single<List<DogBreed>>{
        return api.getDogs()
    }
}