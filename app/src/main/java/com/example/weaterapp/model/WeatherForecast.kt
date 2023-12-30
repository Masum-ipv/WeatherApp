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

data class List(
    @SerializedName("dt")
    var dt: Int,
    @SerializedName("main")
    var main: Main,
    @SerializedName("weather")
    var weather: ArrayList<Weather>,
    @SerializedName("wind")
    var wind: Wind,
    @SerializedName("dt_txt")
    var dtTxt: String
)

data class Wind(
    @SerializedName("speed")
    var speed: Double,
    @SerializedName("deg")
    var deg: Int
)

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
