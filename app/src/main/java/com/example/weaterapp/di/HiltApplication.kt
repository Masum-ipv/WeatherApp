package com.example.weaterapp.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// @HiltAndroidApp triggers Hilt's code generation,
// including a base class for your application that can use dependency injection.

@HiltAndroidApp
class HiltApplication : Application() {
}