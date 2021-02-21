package com.mostafasadati.weathernow

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.lang.Exception

class SettingData {

    companion object {
        val SETTING_FILE_NAME = "Settings"

        fun saveSetting(context: Context) {
            val editor: SharedPreferences.Editor? =
                context.getSharedPreferences(SETTING_FILE_NAME, Context.MODE_PRIVATE)?.edit()
            editor?.putString("CITY", Setting.city)
            editor?.putString("COUNTRY", Setting.country)
            editor?.putFloat("LAT", Setting.lat)
            editor?.putFloat("LON", Setting.lon)
            editor?.putLong("LAST_UPDATE", Setting.lastUpdate)
            editor?.apply()
        }

        fun loadSetting(context: Context) {
            val sharedPref = context.getSharedPreferences(SETTING_FILE_NAME, Context.MODE_PRIVATE)
            Setting.city = sharedPref.getString("CITY", "Tehran")!!
            Setting.country = sharedPref.getString("COUNTRY", "IR")!!
            Setting.unit = stringToUnit(sharedPref.getString("unit", Unit.metric.name)!!)
            Setting.audio = sharedPref.getBoolean("audio", true)
            Setting.widgetColor =
                stringToWidgetColor(sharedPref.getString("widget_color", WidgetColor.light.name)!!)
            Setting.lat = sharedPref.getFloat("LAT", 35.6944f)
            Setting.lon = sharedPref.getFloat("LON", 51.4215f)
            Setting.lastUpdate = sharedPref.getLong("LAST_UPDATE", 0)
        }

         fun stringToUnit(s: String): Unit {
            return try {
                Unit.valueOf(s)
            } catch (ex: Exception) {
                Unit.metric
            }
        }

         fun stringToWidgetColor(s: String): WidgetColor {
            return try {
                WidgetColor.valueOf(s)
            } catch (ex: Exception) {
                WidgetColor.light
            }

        }
    }
}