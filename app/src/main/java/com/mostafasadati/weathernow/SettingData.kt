package com.mostafasadati.weathernow

import android.content.Context
import android.content.SharedPreferences
import com.mostafasadati.weathernow.widgets.WidgetColor
import java.lang.Exception

class SettingData {

    companion object {
        const val SETTING_FILE_NAME = "settings"
        private const val CITY = "city"
        private const val COUNTRY = "country"
        private const val LAT = "lat"
        private const val LON = "lon"
        private const val LAST_UPDATE = "last_update"
        const val AUDIO = "audio"
        const val UNIT = "unit"
        const val CURRENT_WIDGET_COLOR = "current_widget_color"
        const val FORECAST_WIDGET_COLOR = "forecast_widget_color"

        fun saveSetting(context: Context) {
            val editor: SharedPreferences.Editor? =
                context.getSharedPreferences(SETTING_FILE_NAME, Context.MODE_PRIVATE)?.edit()
            editor?.putString(CITY, Setting.city)
            editor?.putString(COUNTRY, Setting.country)
            editor?.putFloat(LAT, Setting.lat)
            editor?.putFloat(LON, Setting.lon)
            editor?.putLong(LAST_UPDATE, Setting.lastUpdate)
            editor?.apply()
        }

        fun loadSetting(context: Context) {
            val sharedPref = context.getSharedPreferences(SETTING_FILE_NAME, Context.MODE_PRIVATE)
            Setting.city = sharedPref.getString(CITY, "Tehran")!!
            Setting.country = sharedPref.getString(COUNTRY, "IR")!!
            Setting.unit = stringToUnit(sharedPref.getString(UNIT, Unit.Metric.name)!!)
            Setting.audio = sharedPref.getBoolean(AUDIO, true)
            Setting.currentWidgetColor =
                stringToWidgetColor(sharedPref.getString(CURRENT_WIDGET_COLOR, WidgetColor.Light.name)!!)
            Setting.forecastWidgetColor =
                stringToWidgetColor(sharedPref.getString(FORECAST_WIDGET_COLOR, WidgetColor.Light.name)!!)
            Setting.lat = sharedPref.getFloat(LAT, 35.6944f)
            Setting.lon = sharedPref.getFloat(LON, 51.4215f)
            Setting.lastUpdate = sharedPref.getLong(LAST_UPDATE, 0)
            Setting.locale = context.resources.configuration.locale
        }

         fun stringToUnit(s: String): Unit {
            return try {
                Unit.valueOf(s)
            } catch (ex: Exception) {
                Unit.Metric
            }
        }

         fun stringToWidgetColor(s: String): WidgetColor {
            return try {
                WidgetColor.valueOf(s.capitalize())
            } catch (ex: Exception) {
                WidgetColor.Light
            }

        }
    }
}