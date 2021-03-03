package com.mostafasadati.weathernow.api

import com.mostafasadati.weathernow.BuildConfig
import com.mostafasadati.weathernow.Setting
import com.mostafasadati.weathernow.model.CurrentWeather
import com.mostafasadati.weathernow.model.ForecastWeather
import com.mostafasadati.weathernow.model.Pollution
import com.mostafasadati.weathernow.model.SearchCity
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
        const val CURRENT = "data/2.5/weather"
        const val FORECAST = "data/2.5/forecast"
        const val POLLUTION = "data/2.5/air_pollution"
        const val SEARCH_NAME = "geo/1.0/direct"
        const val SEARCH_GPS = "geo/1.0/reverse"
        var API_KEY =
            if ((1..2).random() == 1) BuildConfig.OPENWEATHERMAP_API_KEY1 else BuildConfig.OPENWEATHERMAP_API_KEY2
    }

   /* @GET(CURRENT)
    suspend fun getCurrentByName(
        @Query("q") city: String = Setting.city + "," + Setting.country,
        @Query("units") unit: String = Setting.unit.name,
        @Query("appid") apiKey: String = API_KEY
    ): CurrentWeather*/

    @GET(CURRENT)
    suspend fun getCurrentByGPS(
        @Query("lat") lat: Float = Setting.lat,
        @Query("lon") lon: Float = Setting.lon,
        @Query("units") unit: String = Setting.unit.name,
        @Query("appid") apiKey: String = API_KEY
    ): CurrentWeather

/*  @GET(FORECAST)
    suspend fun getForecastByName(
        @Query("q") city: String = Setting.city + "," + Setting.country,
        @Query("units") unit: String = Setting.unit.name,
        @Query("appid") apiKey: String = API_KEY
    ): ForecastWeather*/

    @GET(FORECAST)
    suspend fun getForecastByGPS(
        @Query("lat") lat: Float = Setting.lat,
        @Query("lon") lon: Float = Setting.lon,
        @Query("units") unit: String = Setting.unit.name,
        @Query("appid") apiKey: String = API_KEY
    ): ForecastWeather


    @GET(POLLUTION)
    suspend fun getPollution(
        @Query("lat") lat: Float = Setting.lat,
        @Query("lon") lon: Float = Setting.lon,
        @Query("appid") apiKey: String = API_KEY
    ): Pollution

    @GET(SEARCH_NAME)
    suspend fun searchByName(
        @Query("limit") limit: Int = 5,
        @Query("q") city: String,
        @Query("appid") apiKey: String = API_KEY
    ): List<SearchCity>

    @GET(SEARCH_GPS)
    suspend fun searchByGPS(
        @Query("limit") limit: Int = 5,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = API_KEY
    ): List<SearchCity>

}