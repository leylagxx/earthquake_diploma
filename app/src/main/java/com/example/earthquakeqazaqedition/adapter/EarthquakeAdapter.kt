package com.example.earthquakeqazaqedition.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.earthquakeqazaqedition.databinding.EarthquakeItemBinding
import com.example.earthquakeqazaqedition.model.Earthquake
import com.example.earthquakeqazaqedition.model.EarthquakeResponse



class EarthquakeAdapter : ListAdapter<EarthquakeResponse, EarthquakeAdapter.ViewHolder>(EarthquakeDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            EarthquakeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: EarthquakeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        fun bind(earthquake: EarthquakeResponse){
            binding.magnituda.text = earthquake.features.toString()
        }
    }
}