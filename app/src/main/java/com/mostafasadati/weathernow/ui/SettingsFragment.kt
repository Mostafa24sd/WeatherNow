package com.mostafasadati.weathernow.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.mostafasadati.weathernow.*
import com.mostafasadati.weathernow.Unit

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        preferenceManager.sharedPreferencesName = SettingData.SETTING_FILE_NAME

        setPreferencesFromResource(R.xml.root_preferences, rootKey)

    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences == null)
            return

        if (sharedPreferences.contains(key)) {
            Setting.unit =
                SettingData.stringToUnit(sharedPreferences.getString("unit", Unit.Metric.name)!!)
            Setting.audio = sharedPreferences.getBoolean("audio", true)
            Setting.widgetColor =
                SettingData.stringToWidgetColor(
                    sharedPreferences.getString(
                        "widget_color",
                        WidgetColor.Light.name
                    )!!
                )
        }
    }
}