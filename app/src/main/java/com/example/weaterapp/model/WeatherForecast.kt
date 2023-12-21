package com.example.weaterapp.model

import com.google.gson.annotations.SerializedName

data class WeatherForecast(
    @SerializedName("cod")
    var cod: String,
    @SerializedName("message")
    var message: Int,
    @SerializedName("cnt")
    var cnt: Int,
    @SerializedName("list")
    var list: ArrayList<List>,
    @SerializedName("city")
    var city: City
)
