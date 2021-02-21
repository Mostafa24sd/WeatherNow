package com.mostafasadati.weathernow.data

import androidx.lifecycle.liveData
import com.mostafasadati.weathernow.Resource
import com.mostafasadati.weathernow.api.WeatherApi
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val api: WeatherApi
) {

    fun searchCity(city: String) = liveData(Dispatchers.IO) {
        emit(
            Resource.loading(
                data = null,
                message = "loading"
            )
        )
        try {
            emit(Resource.success(api.searchByName(city = city), message = null))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }

    }
}