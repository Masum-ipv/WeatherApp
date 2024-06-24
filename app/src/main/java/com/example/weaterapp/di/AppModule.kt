package com.example.weaterapp.di

import android.content.Context
import com.example.weaterapp.repository.DataStoreRepository
import com.example.weaterapp.repository.WeatherRepository
import com.example.weaterapp.service.NetworkInterceptor
import com.example.weaterapp.service.WeatherService
import com.example.weaterapp.util.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))


    @Singleton
    @Provides
    fun providesWeatherRepository(weatherService: WeatherService): WeatherRepository =
        WeatherRepository(weatherService)

    @Singleton
    @Provides
    fun providesDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository =
        DataStoreRepository(context)

    @Singleton
    @Provides
    fun providesWeatherService(
        retrofitBuilder: Retrofit.Builder,
        @ApplicationContext context: Context
    ): WeatherService =
        retrofitBuilder
            .client(NetworkInterceptor.getInterceptor(context))
            .build()
            .create(WeatherService::class.java)
}