package com.mostafasadati.weathernow.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var INSTANCE: CurrentDatabase? = null

        fun getInstance(context: Context): CurrentDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CurrentDatabase::class.java, "CurrentDB.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}