package com.example.weaterapp.service

import android.content.Context
import com.example.weaterapp.util.Constants
import com.example.weaterapp.util.Helper
import com.example.weaterapp.util.NoNetworkException
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class NetworkInterceptor {
    companion object {
        fun getInterceptor(context: Context): OkHttpClient {
            //Show network information in to the logcat
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            val clientInterceptor = Interceptor { chain: Interceptor.Chain ->
                var request: Request = chain.request()
                val url: HttpUrl = request.url.newBuilder()
                    .addQueryParameter("appid", Constants.API_KEY)
                    .build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }

            val internetInterceptor = Interceptor { chain: Interceptor.Chain ->
                if (!Helper.isOnline(context)) {
                    throw NoNetworkException()
                } else {
                    val request: Request = chain.request().newBuilder().build()
                    chain.proceed(request)
                }
            }

            val client = OkHttpClient.Builder().apply {
                this.addInterceptor(clientInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(internetInterceptor)
                    // timeout setting
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(25, TimeUnit.SECONDS)
            }.build()
            return client
        }
    }
}