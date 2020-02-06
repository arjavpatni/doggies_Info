package com.arjavp.dogs.model

import io.reactivex.Single
import retrofit2.http.GET

interface DogsApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Single<List<DogBreed>>
    //getDogs() is called, when the retrofit system calls the endpoint in @GET,
    //retrieves info from API and returns as a single (List of DogBreed)
}