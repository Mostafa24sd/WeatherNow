package com.mostafasadati.weathernow.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.mostafasadati.weathernow.data.WeatherRepository

class WeatherViewModel @ViewModelInject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    fun getCurrent() = repository.getCurrent()

    fun getForecast() = repository.getForecast()

    fun getPollution() = repository.getPollution()
}