package com.example.weaterapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weaterapp.repository.DataStoreRepository
import com.example.weaterapp.repository.WeatherRepository
import javax.inject.Inject

// ViewModelFactory is class to instantiate and return ViewModel.
class ViewModelFactory @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val dataStoreRepository: DataStoreRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(weatherRepository) as T
        } else if (modelClass.isAssignableFrom(DataStoreViewModel::class.java)) {
            return DataStoreViewModel(dataStoreRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}