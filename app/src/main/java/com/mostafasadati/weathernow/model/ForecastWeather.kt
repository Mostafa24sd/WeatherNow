package com.mostafasadati.weathernow.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity(tableName = "forecast_table")
data class ForecastWeather(
    @PrimaryKey
    val i: Int,
    @Embedded
    val city: City,
    val cnt: Int,
    val cod: String,
    @SerializedName("list")
    val mList: List<ForecastList>,
    val message: Int
)

data class City(
    @Embedded
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Long,
    val sunrise: Long,
    val sunset: Long,
    val timezone: Int
) {
    data class Coord(
        val lat: Double,
        val lon: Double
    )
}

data class ForecastList(
    @Embedded
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val pop: Float,
    @Embedded
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    @Embedded
    val wind: Wind
)

data class Clouds(
    val all: Int
)

data class Main(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_kf: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Sys(
    val pod: String
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

data class Wind(
    val deg: Int,
    val speed: Double
)

class ConvertForecastList {
    @TypeConverter
    fun restoreList(value: List<ForecastList>): String {
        val gson = Gson()
        val type = object : TypeToken<List<ForecastList>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun saveList(value: String): List<ForecastList> {
        val gson = Gson()
        val type = object : TypeToken<List<ForecastList>>() {}.type
        return gson.fromJson(value, type)
    }
}

class ConvertWeatherList {
    @TypeConverter
    fun restoreList(value: List<Weather>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun saveList(value: String): List<Weather> {
        val gson = Gson()
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(value, type)
    }
}