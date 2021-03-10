package com.mostafasadati.weathernow.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mostafasadati.weathernow.model.Pollution

@Dao
interface PollutionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pollution: Pollution)

    @Query("SELECT * FROM pollution_table WHERE i=0")
    fun get(): Pollution
}