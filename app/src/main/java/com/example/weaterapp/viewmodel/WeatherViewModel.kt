package com.example.weaterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weaterapp.model.CurrentWeather
import com.example.weaterapp.model.WeatherForecast
import com.example.weaterapp.repository.WeatherRepository
import com.example.weaterapp.util.ApiState
import com.example.weaterapp.util.Event
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    val currentWeather: LiveData<Event<ApiState<CurrentWeather>>>
        get() = repository.currentWeather

    val weatherForecast: LiveData<Event<ApiState<WeatherForecast>>>
        get() = repository.weatherForecast

    fun getCurrentWeather(lat: String, lon: String) {
        repository.getCurrentWeather(lat, lon)
    }

    fun getCurrentWeather(city: String) {
        repository.getCurrentWeather(city)
    }

    fun getWeatherForecast(lat: String, lon: String) {
        repository.getWeatherForecast(lat, lon)
    }

    fun getWeatherForecast(city: String) {
        repository.getWeatherForecast(city)
    }

}