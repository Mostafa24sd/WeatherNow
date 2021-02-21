package com.mostafasadati.weathernow.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.lifecycle.ProcessLifecycleOwner
import com.mostafasadati.weathernow.R
import com.mostafasadati.weathernow.Status
import com.mostafasadati.weathernow.data.WeatherRepository
import com.mostafasadati.weathernow.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WidgetProvider : AppWidgetProvider() {
    lateinit var views: RemoteViews

    @Inject
    lateinit var repository: WeatherRepository

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {

        if (appWidgetIds != null) {
            for (id in appWidgetIds) {
                val intent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

                views = RemoteViews(context?.packageName, R.layout.widget_current_layout)
                views.setOnClickPendingIntent(R.id.widget_normal_layout, pendingIntent)

                repository.getCurrent()
                    .observe(ProcessLifecycleOwner.get()) {
                        if (it.status == Status.SUCCESS) {
                            views.setTextViewText(
                                R.id.widgetTxt,
                                it.data?.weather?.get(0)?.description
                            )
                            views.setTextViewText(
                                R.id.widgetTempTxt,
                                it.data?.main?.temp.toString()
                            )
                            views.setImageViewResource(
                                R.id.widgetImg,
                                getIcon(it.data?.weather?.get(0)?.icon!!)
                            )
                            appWidgetManager?.updateAppWidget(appWidgetIds, views)
                            Toast.makeText(context, "Widget Updated", Toast.LENGTH_LONG).show()
                        }
                    }

                appWidgetManager?.updateAppWidget(appWidgetIds, views)
            }
        }
    }

    private fun getIcon(type: String): Int =
        when (type) {
            "01d" -> R.drawable.sunny
            "01n" -> R.drawable.moon
            "02d" -> R.drawable.partly_cloudy1
            "02n", "04n" -> R.drawable.partly_cloudy_night
            "04d" -> R.drawable.partly_cloudy2
            "03d", "03n" -> R.drawable.cloudy1
            "09d", "09n" -> R.drawable.heavy_rain
            "10d", "10n" -> R.drawable.moderate_rain
            "11d" -> R.drawable.thunderstorm
            "11n" -> R.drawable.thunderstorm_night
            "13d" -> R.drawable.light_snowing
            "13n" -> R.drawable.light_snowing_night
            "50d" -> R.drawable.mist
            "50n" -> R.drawable.mist_night
            else -> R.drawable.sunny
        }
}