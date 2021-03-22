package com.mostafasadati.weathernow.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mostafasadati.weathernow.model.ForecastWeather

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forecastWeather: ForecastWeather)

    @Query("SELECT * FROM forecast_table WHERE i=0")
    fun get(): ForecastWeather
}