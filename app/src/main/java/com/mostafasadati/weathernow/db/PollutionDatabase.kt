package com.mostafasadati.weathernow.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mostafasadati.weathernow.model.ConvertPollutionList
import com.mostafasadati.weathernow.model.Pollution

@Database(
    entities = [Pollution::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ConvertPollutionList::class)
abstract class PollutionDatabase : RoomDatabase() {

    abstract fun pollutionDao(): PollutionDao
}