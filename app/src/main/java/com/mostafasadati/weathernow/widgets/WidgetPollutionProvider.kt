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
import com.mostafasadati.weathernow.data.WeatherRepository
import com.mostafasadati.weathernow.model.Pollution
import com.mostafasadati.weathernow.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WidgetPollutionProvider : AppWidgetProvider() {
    lateinit var views: RemoteViews

    @Inject
    lateinit var repository: WeatherRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val intent2 = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0)

        views = RemoteViews(
            context!!.packageName,
            R.layout.widget_pollution_layout
        )

        views.setOnClickPendingIntent(R.id.widget_linear_layout_pollution, pendingIntent)

        views.setInt(
            R.id.p_widget_aqi_text,
            "setTextColor",
            Color.BLACK
        )

        views.setInt(
            R.id.p_widget_description,
            "setTextColor",
            Color.BLACK
        )


        repository.getPollution().observeForever {
            when (it.status) {
                Status.SUCCESS -> setData(context, it)
                Status.LOADING -> setData(context, it)
                Status.ERROR -> {
                    onDisabled(context)
                }
            }
        }
    }

    private fun setData(context: Context, it: Resource<Pollution>) {
        if (it.data != null) {

            views.setInt(
                R.id.widget_linear_layout_pollution,
                "setBackgroundResource",
                getPollutionBackground(it.data.list[0].main.aqi)
            )

            views.setTextViewText(
                R.id.p_widget_aqi_text,
                it.data.aqiIndex
            )

            views.setTextViewText(
                R.id.p_widget_description,
                getPollutionDescription(context,it.data.list[0].main.aqi)
            )
            views.setImageViewResource(
                R.id.p_widget_img,
                getIcon(it.data.list[0].main.aqi)
            )
            AppWidgetManager.getInstance(context).updateAppWidget(
                ComponentName(context, WidgetPollutionProvider::class.java), views
            )

            if (it.status == Status.SUCCESS) {
//                Toast.makeText(context, "P Widget Successful", Toast.LENGTH_SHORT).show()
                return
            }
        }
    }


    private fun getIcon(type: Int) = when (type) {
        1, 2 -> R.drawable.w_p_good_l
        3 -> R.drawable.w_p_moderate_l
        4 -> R.drawable.w_p_poor_l
        5 -> R.drawable.w_p_very_poor_l
        else -> R.drawable.w_p_good_l
    }

    private fun getPollutionBackground(aqi: Int) =
        when (aqi) {
            1 -> R.drawable.bg_p_good
            2 -> R.drawable.bg_p_fair
            3 -> R.drawable.bg_p_moderate
            4 -> R.drawable.bg_p_poor
            5 -> R.drawable.bg_p_very_poor
            else -> R.drawable.bg_p_good
        }
}
