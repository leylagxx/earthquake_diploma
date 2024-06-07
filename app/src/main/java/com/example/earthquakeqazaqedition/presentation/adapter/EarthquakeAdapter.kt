package com.example.earthquakeqazaqedition.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.earthquakeqazaqedition.R
import com.example.earthquakeqazaqedition.databinding.EarthquakeItemBinding
import com.example.earthquakeqazaqedition.data.model.Earthquake
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EarthquakeAdapter : ListAdapter<Earthquake, RecyclerView.ViewHolder>(EarthquakeDiffUtil()) {
    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    private var isLoading = false

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && isLoading) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            ViewHolder(
                EarthquakeItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
        } else {
            LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (isLoading) 1 else 0
    }

    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        if (isLoading) {
            notifyItemInserted(itemCount - 1)
        } else {
            notifyItemRemoved(itemCount)
        }
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
    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
