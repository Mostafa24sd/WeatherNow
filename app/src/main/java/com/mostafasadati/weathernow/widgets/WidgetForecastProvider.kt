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
import com.mostafasadati.weathernow.model.ForecastWeather
import com.mostafasadati.weathernow.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class WidgetForecastProvider : AppWidgetProvider() {
    lateinit var views: RemoteViews

    private var j = 0

    @Inject
    lateinit var repository: WeatherRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val intent2 = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0)


        views = RemoteViews(
            context!!.packageName,
            R.layout.widget_forecast_layout
        )

        views.setOnClickPendingIntent(R.id.f_widget_layout, pendingIntent)

        val daysTextIds = arrayListOf(
            R.id.f_widget_day1,
            R.id.f_widget_day2,
            R.id.f_widget_day3,
            R.id.f_widget_day4
        )

        val tempsTextIds = arrayListOf(
            R.id.f_widget_temp1,
            R.id.f_widget_temp2,
            R.id.f_widget_temp3,
            R.id.f_widget_temp4
        )

        if (Setting.widgetColor == WidgetColor.Dark) {
            views.setInt(
                R.id.f_widget_layout,
                "setBackgroundResource",
                R.drawable.round_corner_dark
            )

            for (id in daysTextIds)
                views.setInt(
                    id,
                    "setTextColor",
                    Color.WHITE
                )
            for (id in tempsTextIds)
                views.setInt(
                    id,
                    "setTextColor",
                    Color.WHITE
                )

        } else {
            views.setInt(
                R.id.f_widget_layout,
                "setBackgroundResource",
                R.drawable.round_corner_light
            )

            for (id in daysTextIds)
                views.setInt(
                    id,
                    "setTextColor",
                    Color.BLACK
                )
            for (id in tempsTextIds)
                views.setInt(
                    id,
                    "setTextColor",
                    Color.BLACK
                )
        }

        repository.getForecast().observeForever {
            when (it.status) {
                Status.SUCCESS -> setData(context, it, daysTextIds, tempsTextIds)
                Status.LOADING -> setData(context, it, daysTextIds, tempsTextIds)
                Status.ERROR -> {
                    onDisabled(context)
                }
            }
        }
    }

    private fun setData(
        context: Context,
        forecastWeather: Resource<ForecastWeather>,
        daysTextIds: ArrayList<Int>,
        tempsTextIds: ArrayList<Int>
    ) {

        if (forecastWeather.data != null) {
            for (i in 0..39) {
                if ((forecastWeather.data.mList[0].dt_txt.substring(
                        0,
                        10
                    )) != forecastWeather.data.mList[i].dt_txt.substring(0, 10)
                ) {
                    j = i
                    break
                }
            }

            val temps = arrayListOf(0, 0, 0, 0)
            val days = arrayListOf<String>()

            for (k in 0..3) {
                for (i in 0..7) {
                    temps[k] += forecastWeather.data.mList[i + j + (k * 8)].main.temp.roundToInt()
                }
                temps[k] /= 8
                days.add(
                    getDayOfWeek(
                        forecastWeather.data.mList[j + (k * 8)].dt_txt.substring(
                            0,
                            10
                        )
                    )
                )
            }

            for (i in 0..3) {
                views.setTextViewText(
                    tempsTextIds[i],
                    setTempUnit(temps[i])
                )

                views.setTextViewText(daysTextIds[i], days[i])

            }

            val imagesIDs = arrayListOf(
                R.id.f_widget_img1,
                R.id.f_widget_img2,
                R.id.f_widget_img3,
                R.id.f_widget_img4,
            )

            for (i in 0..3) {
                views.setImageViewResource(
                    imagesIDs[i],
                    getIcon(forecastWeather.data.mList[i].weather[0].icon)
                )
            }

            AppWidgetManager.getInstance(context).updateAppWidget(
                ComponentName(context, WidgetForecastProvider::class.java), views
            )

            if (forecastWeather.status == Status.SUCCESS) {
                Toast.makeText(context, "F Widget Successful", Toast.LENGTH_SHORT).show()
                return
            }
        }
    }

    private fun getIcon(type: String): Int {
        val icon: Int

        if (Setting.widgetColor == WidgetColor.Dark) {

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
            Unit.Metric -> "$temp \u2103"
            else -> "$temp \u2109"
        }

    private fun getDayOfWeek(str: String?): String {
        var date1: Date? = Date()
        try {
            date1 = SimpleDateFormat("yyyy-MM-dd").parse(str)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val c = Calendar.getInstance()
        c.time = date1
        val dayNum = c[Calendar.DAY_OF_WEEK]
        var dayOfWeek: String? = ""
        when (dayNum) {
            1 -> dayOfWeek = "Sun"
            2 -> dayOfWeek = "Mon"
            3 -> dayOfWeek = "Tue"
            4 -> dayOfWeek = "Wed"
            5 -> dayOfWeek = "Thu"
            6 -> dayOfWeek = "Fri"
            7 -> dayOfWeek = "Sat"
        }
        return dayOfWeek!!
    }
}