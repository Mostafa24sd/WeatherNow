package com.mostafasadati.weathernow.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat
import com.mostafasadati.weathernow.*
import com.mostafasadati.weathernow.Unit
import com.mostafasadati.weathernow.widgets.WidgetColor


class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().theme.applyStyle(R.style.PreferenceScreen, true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

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
                SettingData.stringToUnit(
                    sharedPreferences.getString(
                        SettingData.UNIT,
                        Unit.Metric.name
                    )!!
                )
            Setting.audio = sharedPreferences.getBoolean(SettingData.AUDIO, true)

            Setting.currentWidgetColor =
                SettingData.stringToWidgetColor(
                    sharedPreferences.getString(
                        SettingData.CURRENT_WIDGET_COLOR,
                        WidgetColor.Light.name
                    )!!
                )

            Setting.forecastWidgetColor =
                SettingData.stringToWidgetColor(
                    sharedPreferences.getString(
                        SettingData.FORECAST_WIDGET_COLOR,
                        WidgetColor.Light.name
                    )!!
                )
        }
    }
}