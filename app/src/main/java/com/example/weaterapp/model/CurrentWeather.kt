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

data class Weather(
    @SerializedName("id")
    var id: Int,
    @SerializedName("main")
    var main: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("icon")
    var icon: String
)

data class Main(
    @SerializedName("temp")
    var temp: Double
)
