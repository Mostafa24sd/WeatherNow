package com.mostafasadati.weathernow.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mostafasadati.weathernow.databinding.SearchItemBinding
import com.mostafasadati.weathernow.model.SearchCity

class SearchCityAdapter(
    private val result: List<SearchCity>,
    private val onCityClick: (city: SearchCity) -> Unit
) :
    RecyclerView.Adapter<SearchCityAdapter.SearchCityViewHolder>() {

    private lateinit var binding: SearchItemBinding

    inner class SearchCityViewHolder(private val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(searchCity: SearchCity) {
            binding.city = searchCity
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCityViewHolder {
        binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchCityViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: SearchCityViewHolder, position: Int) {
        val currentItem = result[position]

        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            onCityClick.invoke(currentItem)
        }
    }
}