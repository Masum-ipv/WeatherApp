package com.example.weaterapp.model

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("population")
    var population: Int,
    @SerializedName("timezone")
    var timezone: Int,
    @SerializedName("sunrise")
    var sunrise: Int,
    @SerializedName("sunset")
    var sunset: Int
)
