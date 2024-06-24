package com.example.weaterapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weaterapp.repository.DataStoreRepository
import com.example.weaterapp.util.Constants.TEMP_UNIT
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DataStoreViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel() {

    val tempUnit: LiveData<Boolean?>
        get() = repository.tempUnit

    fun saveTempUnit(value: Boolean) {
        viewModelScope.launch {
            repository.putBoolean(TEMP_UNIT, value)
        }
    }

    fun getTempUnit() = runBlocking {
        repository.getBoolean(TEMP_UNIT)
    }
}
