package com.example.earthquakeqazaqedition.data.network

import com.example.earthquakeqazaqedition.data.model.EarthquakeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("query")
    suspend fun fetchEarthquakes(
        @Query("format") format: String = "geojson",
        @Query("eventtype") eventtype: String = "earthquake",
        @Query("limit") limit: Int,
        @Query("orderby") orderby: String = "time"
    ): EarthquakeResponse
}
