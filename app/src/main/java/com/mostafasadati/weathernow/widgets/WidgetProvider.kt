package com.mostafasadati.weathernow.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import android.widget.Toast
import androidx.lifecycle.Observer
import com.mostafasadati.weathernow.*
import com.mostafasadati.weathernow.Unit
import com.mostafasadati.weathernow.data.WeatherRepository
import com.mostafasadati.weathernow.model.CurrentWeather
import com.mostafasadati.weathernow.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class WidgetProvider : AppWidgetProvider() {
    lateinit var views: RemoteViews

    companion object {
        const val CONSTANT_VALUE = "CURRENT_WIDGET"
    }

    @Inject
    lateinit var repository: WeatherRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val intent2 = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0)

        Toast.makeText(context, "Start", Toast.LENGTH_SHORT).show()

        val views = RemoteViews(
            context!!.packageName,
            R.layout.widget_current_layout
        )

        views.setOnClickPendingIntent(R.id.widget_linear_layout_current, pendingIntent)

        if (Setting.widgetColor == WidgetColor.dark) {
            views.setInt(
                R.id.widget_linear_layout_current,
                "setBackgroundResource",
                R.drawable.round_corner_dark
            )

            views.setInt(
                R.id.widgetTempTxt,
                "setTextColor",
                Color.WHITE
            )

            views.setInt(
                R.id.widgetTxt,
                "setTextColor",
                Color.WHITE
            )
        } else {
            views.setInt(
                R.id.widget_linear_layout_current,
                "setBackgroundResource",
                R.drawable.round_corner_light
            )

            views.setInt(
                R.id.widgetTempTxt,
                "setTextColor",
                Color.BLACK
            )

            views.setInt(
                R.id.widgetTxt,
                "setTextColor",
                Color.BLACK
            )
        }

        repository.getCurrent().observeForever {
            when (it.status) {
                Status.SUCCESS -> {
                    views.setTextViewText(
                        R.id.widgetTxt,
                        it.data?.weather!![0].description.capitalize()
                    )
                    views.setTextViewText(
                        R.id.widgetTempTxt,
                        setTempUnit(it.data.main.temp.roundToInt())
                    )
                    views.setImageViewResource(
                        R.id.widgetImg,
                        getIcon(it.data.weather[0].icon)
                    )
                    AppWidgetManager.getInstance(context).updateAppWidget(
                        ComponentName(context, WidgetProvider::class.java), views
                    )

                    Toast.makeText(context, "Widget Successful", Toast.LENGTH_SHORT).show()
                    onDisabled(context)
                }

                Status.LOADING -> Toast.makeText(context, "Loading", Toast.LENGTH_SHORT)
                    .show()
                Status.ERROR -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    onDisabled(context)
                }
            }
        }
    }

    private fun getIcon(type: String): Int {
        var icon: Int

        if (Setting.widgetColor == WidgetColor.dark) {

            icon = when (type) {
                "01d" -> R.drawable.w_sunny_d
                "01n" -> R.drawable.w_moon_d
                "02d" -> R.drawable.w_partly_cloudy_d
                "02n", "04n" -> R.drawable.w_partly_cloudy_night_d
                "04d" -> R.drawable.w_partly_cloudy_d
                "03d", "03n" -> R.drawable.w_cloudy_d
                "09d", "09n" -> R.drawable.w_heavy_rain_d
                "10d", "10n" -> R.drawable.w_moderate_rain_d
                "11d", "11n" -> R.drawable.w_thunderstorm_d
                "13d", "13n" -> R.drawable.w_light_snowing_d
                "50d", "50n" -> R.drawable.w_mist_d
                else -> R.drawable.w_sunny_d
            }
        } else {
            icon = when (type) {
                "01d" -> R.drawable.w_sunny_l
                "01n" -> R.drawable.w_moon_l
                "02d" -> R.drawable.w_partly_cloudy_l
                "02n", "04n" -> R.drawable.w_partly_cloudy_night_l
                "04d" -> R.drawable.w_partly_cloudy_l
                "03d", "03n" -> R.drawable.w_cloudy_l
                "09d", "09n" -> R.drawable.w_heavy_rain_l
                "10d", "10n" -> R.drawable.w_moderate_rain_l
                "11d", "11n" -> R.drawable.w_thunderstorm_l
                "13d", "13n" -> R.drawable.w_light_snowing_l
                "50d", "50n" -> R.drawable.w_mist_l
                else -> R.drawable.w_sunny_l
            }
        }
        return icon
    }

    private fun setTempUnit(temp: Int) =
        when (Setting.unit) {
            Unit.metric -> "$temp \u2103"
            else -> "$temp \u2109"
        }

}