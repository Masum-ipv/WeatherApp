package com.example.weaterapp.service

import com.example.weaterapp.util.Helper.API_KEY
import com.example.weaterapp.util.Helper.BASE_URL
import com.example.weaterapp.util.Helper.isOnline
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


class RetrofitInstance {
    companion object {
        fun getRetrofitInstance(): Retrofit {

            //Show network information in to the logcat
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            val clientInterceptor = Interceptor { chain: Interceptor.Chain ->
                var request: Request = chain.request()
                val url: HttpUrl = request.url.newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }

//            val internetInterceptor = Interceptor { chain: Interceptor.Chain ->
//                if(!isOnline(context)){
//
//                }
//                val request: Request = chain.request().newBuilder().build()
//                chain.proceed(request)
//            }

            val client = OkHttpClient.Builder().apply {
                this.addInterceptor(clientInterceptor)
                    .addInterceptor(loggingInterceptor)
//                    .addInterceptor(internetInterceptor)
                    // timeout setting
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(25, TimeUnit.SECONDS)
            }.build()


            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
    }
}