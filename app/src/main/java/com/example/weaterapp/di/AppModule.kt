package com.example.weaterapp.di

import com.example.weaterapp.repository.WeatherRepository
import com.example.weaterapp.service.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit = RetrofitInstance.getRetrofitInstance()

    @Singleton
    @Provides
    fun providesRepository(retrofitInstance: Retrofit): WeatherRepository = WeatherRepository(retrofitInstance)
}