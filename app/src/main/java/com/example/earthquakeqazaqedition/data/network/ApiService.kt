package com.example.earthquakeqazaqedition.data.network

import com.example.earthquakeqazaqedition.data.model.EarthquakeResponse
import retrofit2.http.GET

interface ApiService {
    @GET("summary/4.5_day.geojson")
    suspend fun fetchEarthquakes(): EarthquakeResponse

}
