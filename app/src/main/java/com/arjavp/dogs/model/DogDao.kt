package com.arjavp.dogs.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao //Data_access_object
interface DogDao {
    @Insert //this fun will insert data into db.
    suspend fun insertAll(vararg dogs: DogBreed): List<Long>
    //fun will insert multiple DogBreeds and return a list of "primary keys"
    //as defined in model.kt
    //suspend means this fun is to be run on background thread.

    @Query("SELECT * FROM dogbreed")//dogbreed (database name)== DogBreed (model name)
    suspend fun getAllDogs(): List<DogBreed>

    @Query("SELECT * FROM dogbreed WHERE uuid =:dogId")
    suspend fun getDog(dogId: Int): DogBreed

    @Query("DELETE FROM dogbreed")
    suspend fun deleteAlldogs()
}