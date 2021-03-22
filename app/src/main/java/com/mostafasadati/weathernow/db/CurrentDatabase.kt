package com.mostafasadati.weathernow.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mostafasadati.weathernow.model.ConvertList
import com.mostafasadati.weathernow.model.CurrentWeather

@Database(
    entities = [CurrentWeather::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ConvertList::class)
abstract class CurrentDatabase : RoomDatabase() {

    abstract fun currentDao(): CurrentDao
}