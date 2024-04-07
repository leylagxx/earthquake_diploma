package com.example.earthquakeqazaqedition.model

import com.google.gson.annotations.SerializedName

data class Earthquake(
    @SerializedName("id")
    val id: String,
    @SerializedName("mag")
    val mag: Double,
    @SerializedName("place")
    val place: String,
    @SerializedName("time")
    val time: Long,

)
