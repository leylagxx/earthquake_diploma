package com.example.earthquakeqazaqedition.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()

            val newRequest = request.newBuilder()
                .build()
            chain.proceed(newRequest)
        }
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://earthquake.usgs.gov/earthquakes/feed/v1.0/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)


}