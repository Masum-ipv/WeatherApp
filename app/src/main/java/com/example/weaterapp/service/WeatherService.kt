package com.example.weaterapp.service

import com.example.weaterapp.model.CurrentWeather
import com.example.weaterapp.model.WeatherForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather/")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ):
            Response<CurrentWeather>

    @GET("forecast/")
    suspend fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ):
            Response<WeatherForecast>

    @GET("forecast/")
    suspend fun getWeatherForecast(
        @Query("q") city: String
    ):
            Response<WeatherForecast>
}