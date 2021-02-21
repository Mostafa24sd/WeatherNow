package com.mostafasadati.weathernow.data

import android.util.Log
import androidx.lifecycle.liveData
import com.mostafasadati.weathernow.Resource
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
        val a = currentDatabase.currentDao().get()

        if (a == null)
            emit(
                Resource.loading(
                    data = currentDatabase.currentDao().get(),
                    message = "db_null"
                )
            )
        else
            emit(
                Resource.loading(
                    currentDatabase.currentDao().get(),
                    message = "db_full"
                )
            )

        try {
            currentDatabase.currentDao()
                .insert(api.getCurrentByName())
            emit(
                Resource.success(
                    currentDatabase.currentDao().get(),
                    message = null
                )
            )

            Log.d("mosix", "saving....")

        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }
    }

    fun searchByName(city: String) = liveData(Dispatchers.IO) {
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

    fun searchByGPS(latitude: Double, longitudes: Double) = liveData(Dispatchers.IO) {
        emit(
            Resource.loading(
                data = null,
                message = "loading"
            )
        )
        try {
            emit(
                Resource.success(
                    api.searchByGPS(lat = latitude, lon = longitudes),
                    message = null
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }

    }

    fun getForecast() = liveData(Dispatchers.IO) {
        val a = forecastDatabase.forecastDao().get()

        if (a == null)
            emit(
                Resource.loading(
                    data = forecastDatabase.forecastDao().get(),
                    message = "f_db_null"
                )
            )
        else
            emit(
                Resource.loading(
                    forecastDatabase.forecastDao().get(),
                    message = "f_db_full"
                )
            )

        try {
            forecastDatabase.forecastDao()
                .insert(api.getForecastByName())
            emit(
                Resource.success(
                    forecastDatabase.forecastDao().get(),
                    message = null
                )
            )
            Log.d("mosix", "saving...f")
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "f_Error occurred"))
        }
    }

    fun getPollution() = liveData(Dispatchers.IO) {
        val a = pollutionDatabase.pollutionDao().get()

        if (a == null)
            emit(
                Resource.loading(
                    data = pollutionDatabase.pollutionDao().get(),
                    message = "P db_null"
                )
            )
        else
            emit(
                Resource.loading(
                    data = pollutionDatabase.pollutionDao().get(),
                    message = "P db_full"
                )
            )

        try {
            pollutionDatabase.pollutionDao()
                .insert(api.getPollution())
            emit(
                Resource.success(
                    data = pollutionDatabase.pollutionDao().get(),
                    message = null
                )
            )
            Log.d("mosix", "P saving....")
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error occurred"))
        }
    }
}