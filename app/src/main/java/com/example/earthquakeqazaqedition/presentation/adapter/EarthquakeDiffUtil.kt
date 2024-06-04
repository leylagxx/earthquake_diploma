package com.example.earthquakeqazaqedition.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.earthquakeqazaqedition.data.model.Earthquake

class EarthquakeDiffUtil : DiffUtil.ItemCallback<Earthquake>(){
    override fun areItemsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
        return oldItem == newItem
    }

}