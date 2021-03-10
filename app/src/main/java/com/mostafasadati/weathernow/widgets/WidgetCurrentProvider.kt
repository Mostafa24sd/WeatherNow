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
import com.mostafasadati.weathernow.*
import com.mostafasadati.weathernow.Unit
import com.mostafasadati.weathernow.data.WeatherRepository
import com.mostafasadati.weathernow.model.CurrentWeather
import com.mostafasadati.weathernow.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class WidgetCurrentProvider : AppWidgetProvider() {
    lateinit var views: RemoteViews

    @Inject
    lateinit var repository: WeatherRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val intent2 = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0)

        views = RemoteViews(
            context!!.packageName,
            R.layout.widget_current_layout
        )

        views.setOnClickPendingIntent(R.id.widget_linear_layout_current, pendingIntent)

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


        repository.getCurrent().observeForever {
            when (it.status) {
                Status.SUCCESS -> setData(context, it)
                Status.LOADING -> setData(context, it)
                Status.ERROR -> {
                    onDisabled(context)
                }
            }
        }
    }

    private fun setData(context: Context, it: Resource<CurrentWeather>) {
        if (it.data != null) {

            views.setInt(
                R.id.w_current_bg,
                "setImageResource",
                getBackground(it.data.weather[0].icon)
            )


            views.setTextViewText(
                R.id.widgetTxt,
                it.data.weather[0].description.capitalize()
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
                ComponentName(context, WidgetCurrentProvider::class.java), views
            )

            if (it.status == Status.SUCCESS) {
                Toast.makeText(context, "Widget Successful", Toast.LENGTH_SHORT).show()
                return
            }
        }
    }

    private fun getIcon(type: String) =
        when (type) {
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

    private fun getBackground(icon: String): Int {
        return when (icon) {
            "01d" -> R.drawable.bg_sunny
            "01n" -> R.drawable.bg_clear_night
            "02d" -> R.drawable.bg_few_clouds_day
            "02n" -> R.drawable.bg_few_clouds_night
            "03d" -> R.drawable.bg_scattered_day
            "03n" -> R.drawable.bg_scattered_night
            "04d" -> R.drawable.bg_broken_clouds_day
            "04n" -> R.drawable.bg_broken_clouds_night
            "09d", "09n", "10d", "10n" -> R.drawable.bg_rain
            "11d" -> R.drawable.bg_thunder_day
            "11n" -> R.drawable.bg_thunder_night
            "13d", "13n" -> R.drawable.bg_snow
            "50d" -> R.drawable.bg_mist
            "50n" -> R.drawable.bg_mist_night
            else -> R.drawable.ic_baseline_refresh_24
        }
    }

    private fun setTempUnit(temp: Int) =
        when (Setting.unit) {
            Unit.Metric -> "$temp \u2103"
            else -> "$temp \u2109"
        }

}