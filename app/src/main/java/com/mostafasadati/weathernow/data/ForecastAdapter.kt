package com.mostafasadati.weathernow.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mostafasadati.weathernow.databinding.ForecastItemsBinding
import com.mostafasadati.weathernow.model.ForecastList
import com.mostafasadati.weathernow.model.ForecastWeather

class ForecastAdapter(private val forecastWeather: ForecastWeather) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private lateinit var binding: ForecastItemsBinding

    class ForecastViewHolder(private val binding: ForecastItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecast: ForecastList) {
            binding.forecast = forecast
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        binding = ForecastItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val currentItem = forecastWeather.mList[position]

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return forecastWeather.mList.size
    }

}