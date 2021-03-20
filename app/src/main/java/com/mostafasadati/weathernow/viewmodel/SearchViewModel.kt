package com.mostafasadati.weathernow.viewmodel

import androidx.lifecycle.ViewModel
import com.mostafasadati.weathernow.data.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    fun searchByName(city: String) = repository.searchByName(city)

    fun searchByGPS(latitude:Double,longitudes:Double) = repository.searchByGPS(latitude,longitudes)
}
