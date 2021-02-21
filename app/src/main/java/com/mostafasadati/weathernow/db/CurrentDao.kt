package com.mostafasadati.weathernow.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mostafasadati.weathernow.Resource
import com.mostafasadati.weathernow.Setting
import com.mostafasadati.weathernow.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeather: CurrentWeather)

    @Query("SELECT * FROM current_weather WHERE name = :city AND sys_country= :country")
    fun get(city: String = Setting.city, country: String = Setting.country): CurrentWeather
}