package com.example.weaterapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weaterapp.model.CurrentWeather
import com.example.weaterapp.model.WeatherForecast
import com.example.weaterapp.service.RetrofitInstance
import com.example.weaterapp.service.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherRepository {
    val appid = "8f1460350c952343bddcd03c5d09be4d"

    var weatherService: WeatherService = RetrofitInstance
        .getRetrofitInstance()
        .create(WeatherService::class.java)

    fun getCurrentWeather(lat: String, lon: String): LiveData<CurrentWeather> {
        var weatherData = MutableLiveData<CurrentWeather>()

        GlobalScope.launch(Dispatchers.IO) {
            val response = weatherService.getCurrentWeather(lat, lon, appid)
            if (response != null) {
                weatherData.postValue(response.body())
            }
        }
        return weatherData
    }

    fun getWeatherForecast(lat: String, lon: String): LiveData<WeatherForecast> {
        var weatherData = MutableLiveData<WeatherForecast>()

        GlobalScope.launch(Dispatchers.IO) {
            val response = weatherService.getWeatherForecast(lat, lon, appid)
            if (response != null) {
                weatherData.postValue(response.body())
            }
        }
        return weatherData
    }

    fun getWeatherForecast(city: String): LiveData<WeatherForecast> {
        var weatherData = MutableLiveData<WeatherForecast>()

        GlobalScope.launch(Dispatchers.IO) {
            val response = weatherService.getWeatherForecast(city, appid)
            if (response != null) {
                weatherData.postValue(response.body())
            }
        }
        return weatherData
    }
}