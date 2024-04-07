package com.example.earthquakeqazaqedition.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName



data class Geometry(
  @SerializedName("type")
  val type: String,
  @SerializedName("coordinates")
  val coordinates: List<Double>
)