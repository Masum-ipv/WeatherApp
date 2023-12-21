package com.example.weaterapp.model

import com.google.gson.annotations.SerializedName

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