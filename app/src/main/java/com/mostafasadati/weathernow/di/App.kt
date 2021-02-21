package com.mostafasadati.weathernow.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.mostafasadati.weathernow.SettingData
import com.mostafasadati.weathernow.api.WeatherApi
import com.mostafasadati.weathernow.db.CurrentDatabase
import com.mostafasadati.weathernow.db.ForecastDatabase
import com.mostafasadati.weathernow.db.PollutionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        SettingData.loadSetting(this)
    }

    @Module
    @InstallIn(ApplicationComponent::class)
    object AppModule {

        @Provides
        @Singleton
        fun provideClient(): OkHttpClient {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            return OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
        }

        @Provides
        @Singleton
        fun provideApi(client: OkHttpClient): WeatherApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(WeatherApi.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(WeatherApi::class.java)
        }

        @Singleton
        @Provides
        fun provideCurrentDB(@ApplicationContext app: Context) =
            Room.databaseBuilder(
                app,
                CurrentDatabase::class.java,
                "CurrentDB.db"
            ).build()

        @Singleton
        @Provides
        fun provideCurrentDao(db: CurrentDatabase) =
            db.currentDao()

        @Singleton
        @Provides
        fun provideForecastDB(@ApplicationContext app: Context) =
            Room.databaseBuilder(
                app,
                ForecastDatabase::class.java,
                "ForecastDB.db"
            ).build()

        @Singleton
        @Provides
        fun provideForecastDao(db: ForecastDatabase) =
            db.forecastDao()

        @Singleton
        @Provides
        fun providePollutionDB(@ApplicationContext app: Context) =
            Room.databaseBuilder(
                app,
                PollutionDatabase::class.java,
                "PollutionDB.db"
            ).build()

        @Singleton
        @Provides
        fun providePollutionDao(db: PollutionDatabase) =
            db.pollutionDao()
    }
}