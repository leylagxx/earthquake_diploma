package com.example.earthquakeqazaqedition.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.earthquakeqazaqedition.R
import com.example.earthquakeqazaqedition.databinding.EarthquakeItemBinding
import com.example.earthquakeqazaqedition.data.model.Earthquake

class EarthquakeAdapter : ListAdapter<Earthquake, EarthquakeAdapter.ViewHolder>(EarthquakeDiffUtil()) {

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
        fun bind(earthquake: Earthquake) {
            with(binding) {
                magnituda.text = earthquake.properties.mag.toString()
                location.text = earthquake.properties.place
            }
        }
    }
}
