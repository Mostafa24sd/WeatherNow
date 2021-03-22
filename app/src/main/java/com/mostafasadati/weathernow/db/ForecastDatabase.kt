package com.mostafasadati.weathernow.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mostafasadati.weathernow.model.ConvertForecastList
import com.mostafasadati.weathernow.model.ConvertWeatherList
import com.mostafasadati.weathernow.model.ForecastWeather

@Database(
    entities = [ForecastWeather::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ConvertForecastList::class, ConvertWeatherList::class)
abstract class ForecastDatabase : RoomDatabase() {

    abstract fun forecastDao(): ForecastDao
}