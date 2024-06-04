package com.example.earthquakeqazaqedition.data.network

import com.example.earthquakeqazaqedition.data.model.EarthquakeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("summary/all_hour.geojson")
    suspend fun fetchEarthquakes(
    ): EarthquakeResponse

}
