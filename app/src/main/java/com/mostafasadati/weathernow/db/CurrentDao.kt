package com.mostafasadati.weathernow.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mostafasadati.weathernow.model.CurrentWeather

@Dao
interface CurrentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeather: CurrentWeather)

    @Query("SELECT * FROM current_weather WHERE i = 0")
    fun get(): CurrentWeather
}