package com.example.weaterapp.util

sealed class ApiState<T>(val data: T? = null, val errorMessage: String? = null) {
    class Loading<T> : ApiState<T>()
    class Success<T>(data: T? = null) : ApiState<T>(data = data)
    class Error<T>(errorMessage: String) : ApiState<T>(errorMessage = errorMessage)
}
