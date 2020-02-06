package com.arjavp.dogs.model

import com.google.gson.annotations.SerializedName

data class DogBreed(
    //serialized names are the corresponding "keys" from JSON (API)
    @SerializedName("id")
    val breedId: String?,

    @SerializedName("name")
    val dogBreed: String?,

    @SerializedName("life_span")
    val lifeSpan: String?,

    @SerializedName("breed_group")
    val breedGroup: String?,

    @SerializedName("bred_for")
    val bredFor: String?,

    @SerializedName("temperament")
    val temperament: String?,

    @SerializedName("url")
    val imageUrl: String?
)