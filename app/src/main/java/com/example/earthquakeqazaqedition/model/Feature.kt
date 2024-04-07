package com.example.earthquakeqazaqedition.model


import com.google.gson.annotations.SerializedName



data class Feature(
  @SerializedName("type")
  val typeString: String,
  @SerializedName("properties")
  val properties: Properties,
  @SerializedName("geometry")
  val geometry: Geometry,
  @SerializedName("id")
  val id: String
)