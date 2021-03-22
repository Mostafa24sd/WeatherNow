package com.mostafasadati.weathernow.data

import android.util.Log
import androidx.lifecycle.liveData
import com.mostafasadati.weathernow.Resource
import com.mostafasadati.weathernow.Setting
import com.mostafasadati.weathernow.api.WeatherApi
import com.mostafasadati.weathernow.db.CurrentDatabase
import com.mostafasadati.weathernow.db.ForecastDatabase
import com.mostafasadati.weathernow.db.PollutionDatabase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val currentDatabase: CurrentDatabase,
    private val pollutionDatabase: PollutionDatabase,
    private val forecastDatabase: ForecastDatabase
) {

    fun getCurrent() = liveData(Dispatchers.IO) {
        val db = currentDatabase.currentDao().get()

        if (db == null)
            emit(
                Resource.loadingDbNull(
                    data = currentDatabase.currentDao().get()
                )
            )
        else
            emit(
                Resource.loadingDbFull(
                    data = currentDatabase.currentDao().get()
                )
            )

        if (Setting.SHOULD_UPDATE) {
            try {
                currentDatabase.currentDao()
                    .insert(api.getCurrentByGPS())
                emit(
                    Resource.success(
                        currentDatabase.currentDao().get()
                    )
                )

            } catch (exception: Exception) {
                emit(Resource.error(data = null))
                exception.printStackTrace()
            }
            Setting.SHOULD_UPDATE = false
        }
    }

    fun getForecast() = liveData(Dispatchers.IO) {
        val db = forecastDatabase.forecastDao().get()

        if (db == null)
            emit(
                Resource.loadingDbNull(
                    data = forecastDatabase.forecastDao().get()
                )
            )
        else
            emit(
                Resource.loadingDbFull(
                    forecastDatabase.forecastDao().get()
                )
            )

        if (Setting.SHOULD_UPDATE) {
            try {
                forecastDatabase.forecastDao()
                    .insert(api.getForecastByGPS())
                emit(
                    Resource.success(
                        forecastDatabase.forecastDao().get()
                    )
                )
            } catch (exception: Exception) {
                emit(Resource.error(data = null))
            }
        }
    }

    fun getPollution() = liveData(Dispatchers.IO) {
        val db = pollutionDatabase.pollutionDao().get()

        if (db == null)
            emit(
                Resource.loadingDbNull(
                    data = pollutionDatabase.pollutionDao().get()
                )
            )
        else
            emit(
                Resource.loadingDbFull(
                    data = pollutionDatabase.pollutionDao().get()
                )
            )

        try {
            pollutionDatabase.pollutionDao()
                .insert(api.getPollution())
            emit(
                Resource.success(
                    data = pollutionDatabase.pollutionDao().get()
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null))
        }
    }

    fun searchByName(city: String) = liveData(Dispatchers.IO) {
        emit(
            Resource.loading(
                data = null
            )
        )
        try {
            emit(Resource.success(api.searchByName(city = city)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null))
        }

    }

    fun searchByGPS(latitude: Double, longitudes: Double) = liveData(Dispatchers.IO) {
        emit(
            Resource.loading(
                data = null
            )
        )
        try {
            emit(
                Resource.success(
                    api.searchByGPS(lat = latitude, lon = longitudes)
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null))
        }

    }
}