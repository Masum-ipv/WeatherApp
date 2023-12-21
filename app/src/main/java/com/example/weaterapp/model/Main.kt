package com.example.weaterapp.model

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp") var temp: Double
)
