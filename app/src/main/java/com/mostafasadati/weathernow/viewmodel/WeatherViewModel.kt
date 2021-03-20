package com.mostafasadati.weathernow.viewmodel

import androidx.lifecycle.ViewModel
import com.mostafasadati.weathernow.data.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    fun getCurrent() = repository.getCurrent()

    fun getForecast() = repository.getForecast()

    fun getPollution() = repository.getPollution()
}