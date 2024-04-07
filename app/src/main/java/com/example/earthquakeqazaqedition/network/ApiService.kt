package com.example.earthquakeqazaqedition.network


import com.example.earthquakeqazaqedition.model.Earthquake
import com.example.earthquakeqazaqedition.model.EarthquakeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("4.5_hour.geojson")
    fun fetchEarthquakes() : Call<List<EarthquakeResponse>>
}