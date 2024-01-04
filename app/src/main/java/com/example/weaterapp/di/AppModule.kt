package com.example.weaterapp.di

import android.content.Context
import com.example.weaterapp.repository.WeatherRepository
import com.example.weaterapp.service.RetrofitInstance
import com.example.weaterapp.service.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit =
        RetrofitInstance.getRetrofitInstance(context)

    @Singleton
    @Provides
    fun providesRepository(retrofitInstance: Retrofit): WeatherRepository =
        WeatherRepository(retrofitInstance)

    fun providesWeatherService(
        @ApplicationContext context: Context,
        retrofitBuilder: Retrofit.Builder
    ): WeatherService =
        retrofitBuilder.build().create(WeatherService::class.java)
}