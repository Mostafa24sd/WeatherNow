package com.mostafasadati.weathernow.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.mostafasadati.weathernow.data.WeatherRepository

class SearchViewModel @ViewModelInject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    fun searchByName(city: String) = repository.searchByName(city)

    fun searchByGPS(latitude:Double,longitudes:Double) = repository.searchByGPS(latitude,longitudes)
}
