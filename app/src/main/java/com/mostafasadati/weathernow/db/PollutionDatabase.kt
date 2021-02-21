package com.mostafasadati.weathernow.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var INSTANCE: PollutionDatabase? = null

        fun getInstance(context: Context): PollutionDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PollutionDatabase::class.java, "PollutionDB.db"
            ).build()
    }
}