package com.example.earthquakeqazaqedition.network

import com.example.earthquakeqazaqedition.model.EarthquakeResponse
import retrofit2.http.GET

interface ApiService {
    @GET("summary/4.5_day.geojson")
    suspend fun fetchEarthquakes(): EarthquakeResponse

}
