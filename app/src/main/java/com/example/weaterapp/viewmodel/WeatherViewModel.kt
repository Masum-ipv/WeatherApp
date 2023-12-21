package com.example.weaterapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weaterapp.model.CurrentWeather
import com.example.weaterapp.model.WeatherForecast
import com.example.weaterapp.repository.WeatherRepository

class WeatherViewModel : ViewModel() {

    private val repository: WeatherRepository = WeatherRepository()

    fun getCurrentWeather(lat: String, lon: String): LiveData<CurrentWeather> {
        return repository.getCurrentWeather(lat, lon)
    }

    fun getWeatherForecast(lat: String, lon: String): LiveData<WeatherForecast> {
        return repository.getWeatherForecast(lat, lon)
    }

    fun getWeatherForecast(city: String): LiveData<WeatherForecast> {
        return repository.getWeatherForecast(city)
    }

}