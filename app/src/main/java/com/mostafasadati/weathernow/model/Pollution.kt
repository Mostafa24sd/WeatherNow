package com.mostafasadati.weathernow.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.mostafasadati.weathernow.PollutionCalculator

@Entity(tableName = "pollution_table")
data class Pollution(
    @PrimaryKey
    var i: Int,
    @SerializedName("list")
    val list: List<PollutionList>,
    @Embedded
    val coord: Coord
) {
    val aqiIndex: String
        get() {
            return PollutionCalculator.calculateAqi(list[0].components.pm2_5).toString()
        }
}

data class Coord(
    val lon: Float,
    val lat: Float
)

data class PollutionList(
    @Embedded
    val components: Components,
    val dt: Int,
    @Embedded
    @SerializedName("main")
    val main: MainPollution
)

data class Components(
    val co: Double,
    val nh3: Double,
    val no: Double,
    val no2: Double,
    val o3: Double,
    val pm10: Double,
    val pm2_5: Double,
    val so2: Double
)

data class MainPollution(
    val aqi: Int
)

class ConvertPollutionList {
    @TypeConverter
    fun restoreList(value: List<PollutionList>): String {
        val gson = Gson()
        val type = object : TypeToken<List<PollutionList>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun saveList(value: String): List<PollutionList> {
        val gson = Gson()
        val type = object : TypeToken<List<PollutionList>>() {}.type
        return gson.fromJson(value, type)
    }
}