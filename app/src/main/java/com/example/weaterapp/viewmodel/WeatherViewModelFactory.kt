package com.example.weaterapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weaterapp.repository.WeatherRepository
import javax.inject.Inject

// ViewModelFactory is class to instantiate and return ViewModel.
class WeatherViewModelFactory @Inject constructor(
    private val repository: WeatherRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}