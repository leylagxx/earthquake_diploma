package com.example.earthquakeqazaqedition.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.earthquakeqazaqedition.R
import com.example.earthquakeqazaqedition.databinding.EarthquakeItemBinding
import com.example.earthquakeqazaqedition.data.model.Earthquake
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EarthquakeAdapter : ListAdapter<Earthquake, RecyclerView.ViewHolder>(EarthquakeDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            EarthquakeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder){
            return holder.bind(getItem(position))
        }
    }



    inner class ViewHolder(private val binding: EarthquakeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        fun bind(earthquake: Earthquake) {
            with(binding) {
                magnituda.text = "M ${earthquake.properties.mag.toString()}"
                location.text = earthquake.properties.place
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = earthquake.properties.time
                val format = SimpleDateFormat("E, MMM d, yyyy  hh:mm", Locale.getDefault())
                time.text = format.format(calendar.time)
            }
        }
    }
}
