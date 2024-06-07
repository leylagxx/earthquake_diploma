package com.example.earthquakeqazaqedition.data.model

import com.google.gson.annotations.SerializedName

data class Feature(
    @SerializedName("type")
    val type: String,
    @SerializedName("properties")
    val properties: Properties,
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("id")
    val id: String
) {
    companion object {
        fun toEarthquake(feature: Feature) = Earthquake(
            id = feature.id,
            properties = feature.properties,
            type = feature.type,
            geometry = feature.geometry
        )
    }
}


data class EarthquakeResponse(
    @SerializedName("features")
    val features: List<Feature>,
    @SerializedName("metadata")
    val metadata: Metadata
)

data class Metadata(
    @SerializedName("generated")
    val generated: Long,
    @SerializedName("url")
    val url: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("api")
    val api: String,
    @SerializedName("count")
    val count: Int,
    @SerializedName("status")
    val status: Int
)