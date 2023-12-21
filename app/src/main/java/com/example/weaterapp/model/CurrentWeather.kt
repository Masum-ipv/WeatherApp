package com.example.weaterapp.model

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("weather")
    var weather: ArrayList<Weather>,
    @SerializedName("main")
    var main: Main,
    @SerializedName("wind")
    var wind: Wind,
    @SerializedName("name")
    var name: String
)
