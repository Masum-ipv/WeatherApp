package com.example.weaterapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.weaterapp.model.CurrentWeather
import com.example.weaterapp.model.WeatherForecast
import com.example.weaterapp.service.WeatherService
import com.example.weaterapp.util.ApiState
import com.example.weaterapp.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherService: WeatherService) {

    val currentWeather = MutableLiveData<Event<ApiState<CurrentWeather>>>()
    val weatherForecast = MutableLiveData<Event<ApiState<WeatherForecast>>>()

    fun getCurrentWeather(lat: String, lon: String) {
        currentWeather.postValue(Event(ApiState.Loading()))
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = weatherService.getCurrentWeather(lat, lon)
                if (response.isSuccessful) {
                    currentWeather.postValue(Event(ApiState.Success(response.body())))
                } else {
                    currentWeather.postValue(Event(ApiState.Error(response.message())))
                }
            } catch (e: Exception) {
                currentWeather.postValue(Event(ApiState.Error(e.message!!)))
            }
        }
    }

    fun getCurrentWeather(city: String) {
        currentWeather.postValue(Event(ApiState.Loading()))
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = weatherService.getCurrentWeather(city)
                if (response.isSuccessful) {
                    currentWeather.postValue(Event(ApiState.Success(response.body())))
                } else {
                    currentWeather.postValue(Event(ApiState.Error(response.message())))
                }
            } catch (e: Exception) {
                currentWeather.postValue(Event(ApiState.Error(e.message!!)))
            }
        }
    }

    fun getWeatherForecast(lat: String, lon: String) {
        weatherForecast.postValue(Event(ApiState.Loading()))
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = weatherService.getWeatherForecast(lat, lon)
                if (response.isSuccessful) {
                    weatherForecast.postValue(Event(ApiState.Success(response.body())))
                } else {
                    weatherForecast.postValue(Event(ApiState.Error(response.message())))
                }
            } catch (e: Exception) {
                currentWeather.postValue(Event(ApiState.Error(e.message!!)))
            }
        }
    }

    fun getWeatherForecast(city: String) {
        weatherForecast.postValue(Event(ApiState.Loading()))
        GlobalScope.launch(Dispatchers.IO) {
            try {

                val response = weatherService.getWeatherForecast(city)
                if (response.isSuccessful) {
                    weatherForecast.postValue(Event(ApiState.Success(response.body())))
                } else {
                    weatherForecast.postValue(Event(ApiState.Error(response.message())))
                }
            } catch (e: Exception) {
                currentWeather.postValue(Event(ApiState.Error(e.message!!)))
            }
        }
    }
}