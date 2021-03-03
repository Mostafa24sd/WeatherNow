package com.mostafasadati.weathernow.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "current_weather")
data class CurrentWeather(
    @PrimaryKey
    var i: Int,
    val id: Int,
    val base: String,
    @Embedded(prefix = "clouds_")
    val clouds: Clouds,
    val cod: Int,
    @Embedded(prefix = "coord_")
    val coord: Coord,
    val dt: Long,
    @Embedded(prefix = "main_")
    val main: Main,
    val name: String,
    @Embedded(prefix = "sys_")
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    @Embedded(prefix = "wind_")
    val wind: Wind,
) {

    data class Clouds(
        val all: Int
    )

    data class Coord(
        val lat: Double,
        val lon: Double
    )

    data class Main(
        val feels_like: Double,
        val humidity: Int,
        val pressure: Int,
        val temp: Double,
        val temp_max: Double,
        val temp_min: Double
    )

    data class Sys(
        val country: String,
        val id: Int,
        val sunrise: Long,
        val sunset: Long,
        val type: Int
    )

    data class Weather(
        val description: String,
        val id: Int,
        val main: String,
        val icon: String
    )

    data class Wind(
        val deg: Int,
        val speed: Double
    )
}

class ConvertList {
    @TypeConverter
    fun restoreList(value: List<CurrentWeather.Weather>): String {
        val gson = Gson()
        val type = object : TypeToken<List<CurrentWeather.Weather>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun saveList(value: String): List<CurrentWeather.Weather> {
        val gson = Gson()
        val type = object : TypeToken<List<CurrentWeather.Weather>>() {}.type
        return gson.fromJson(value, type)
    }
}