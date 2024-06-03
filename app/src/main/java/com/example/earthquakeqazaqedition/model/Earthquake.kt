package com.example.earthquakeqazaqedition.model

data class Earthquake(
    val id: String,
    val type: String,
    val properties: Properties,
    val geometry: Geometry
)

data class Properties(
    val mag: Double,
    val place: String,
    val time: Long,
    val updated: Long,
    val tz: Int?,
    val url: String,
    val detail: String,
    val felt: Int?,
    val cdi: Double?,
    val mmi: Double?,
    val alert: String?,
    val status: String,
    val tsunami: Int,
    val sig: Int,
    val net: String,
    val code: String,
    val ids: String,
    val sources: String,
    val types: String,
    val nst: Int,
    val dmin: Double?,
    val rms: Double,
    val gap: Int,
    val magType: String,
    val title: String
)

data class Geometry(
    val type: String,
    val coordinates: List<Double>
)
