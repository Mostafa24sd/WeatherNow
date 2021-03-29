package com.mostafasadati.weathernow.widgets

import android.content.Context
import android.util.Log
import com.mostafasadati.weathernow.getDayOfWeek
import com.mostafasadati.weathernow.model.ForecastWeather
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class ForecastCalculator(val context: Context, val forecastWeather: ForecastWeather) {
    private var date: Calendar = Calendar.getInstance()

    init {
        date[Calendar.HOUR_OF_DAY] = 0
        date[Calendar.MINUTE] = 0
        date[Calendar.SECOND] = 0
        date[Calendar.MILLISECOND] = 0
    }

    fun getTempAndDates(): Pair<ArrayList<Int>, ArrayList<String>> {
        val (tomorrow, _) = getDays()

        var startIndex = 0

        for (i in forecastWeather.mList.indices) {
            if (forecastWeather.mList[i].dt >= tomorrow) {
                startIndex = i
                break
            }
        }

        val temps = arrayListOf(0, 0, 0, 0)
        val days = arrayListOf<String>()

        for (i in 0..3) {
            for (k in 0..7) {
                temps[i] += forecastWeather.mList[startIndex + (k * (i + 1))].main.temp.roundToInt()
            }
            temps[i] = temps[i] / 8
            days.add(
                getDayOfWeek(
                    context,
                    forecastWeather.mList[startIndex + (i * 8)].dt_txt
                )!!
            )
        }
        return Pair(temps, days)
    }

    private fun getDays(): Pair<Long, Long> {
        date.add(Calendar.DAY_OF_MONTH, 1)
        val tomorrow = date.timeInMillis / 1000
        date.add(Calendar.DAY_OF_MONTH, 1)
        val dayAfterTomorrow = date.timeInMillis / 1000

        return Pair(tomorrow, dayAfterTomorrow)
    }

}