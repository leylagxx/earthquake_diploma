package com.example.earthquakeqazaqedition.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.earthquakeqazaqedition.model.Earthquake
import com.example.earthquakeqazaqedition.model.EarthquakeResponse

class EarthquakeDiffUtil : DiffUtil.ItemCallback<EarthquakeResponse>(){
    override fun areItemsTheSame(oldItem: EarthquakeResponse, newItem: EarthquakeResponse): Boolean {
        return oldItem.features.toString() == newItem.features.toString()
    }

    override fun areContentsTheSame(oldItem: EarthquakeResponse, newItem: EarthquakeResponse): Boolean {
        return oldItem == newItem
    }

}