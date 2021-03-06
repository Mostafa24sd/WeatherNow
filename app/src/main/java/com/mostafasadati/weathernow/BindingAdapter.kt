package com.mostafasadati.weathernow

import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.util.Log
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


@BindingAdapter("setIcon")
fun setIcon(imageView: ImageView, type: String) {
    imageView.clearAnimation()
    if (type != "")
        when (type) {
            "01d" -> {
                imageView.setImageResource(R.drawable.sunny)
                val rotate = RotateAnimation(
                    0f,
                    360f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                rotate.duration = 5000
                rotate.interpolator = LinearInterpolator()
                rotate.repeatCount = Animation.INFINITE
                imageView.startAnimation(rotate)
            }
            "01n" -> {
                imageView.setImageResource(R.drawable.moon)
                imageView.startAnimation(
                    AnimationUtils.loadAnimation(
                        imageView.context,
                        R.anim.rotate_swing
                    )
                )
            }
            "02d" -> {
                imageView.setImageResource(R.drawable.partly_cloudy1_anim)
                (imageView.drawable as AnimationDrawable).start()
            }
            "02n", "04n" -> imageView.setImageResource(R.drawable.partly_cloudy_night)
            "04d" -> imageView.setImageResource(R.drawable.partly_cloudy2)
            "03d", "03n" -> imageView.setImageResource(R.drawable.cloudy1)
            "09d" -> {
                imageView.setImageResource(R.drawable.heavy_rain_anim)
                (imageView.drawable as AnimationDrawable).start()
            }
            "09n" -> imageView.setImageResource(R.drawable.heavy_rain)
            "10d", "10n" -> imageView.setImageResource(R.drawable.moderate_rain)
            "11d" -> imageView.setImageResource(R.drawable.thunderstorm)
            "11n" -> imageView.setImageResource(R.drawable.thunderstorm_night)
            "13d" -> imageView.setImageResource(R.drawable.light_snowing)
            "13n" -> imageView.setImageResource(R.drawable.light_snowing_night)
            "50d" -> imageView.setImageResource(R.drawable.mist)
            "50n" -> imageView.setImageResource(R.drawable.mist_night)
        }
}

@BindingAdapter("setAnim")
fun setAnim(anim: LottieAnimationView, type: String) {

    if (type != "null") {
        val animationResource =
            when (type) {
                "01d" -> "sunny.json"
                "01n" -> "clear_night.json"
                "02d" -> "foggy.json"
                "02n" -> "cloudy_night.json"
                "03d", "03n", "04d", "04n" -> "cloud.json"
                "09d", "09n" -> "rain.json"
                "10d" -> "rain_day.json"
                "10n" -> "rain_night.json"
                "11d", "11n" -> "storm.json"
                "13d" -> "snow_day.json"
                "13n" -> "snow_night.json"
                "50d", "50n" -> "mist.json"
                else -> "sunny.json"
            }


        anim.setAnimation(animationResource)
        anim.playAnimation()
    }
}

@BindingAdapter("setTopBg")
fun setTopBg(view: ImageView, type: String) {
    if (type != "")
        when (type) {
            "01d" -> view.setImageResource(R.drawable.background_sunny)
            "01n", "02n", "04n", "03n", "09n", "10n", "11n", "13n" -> view.setImageResource(R.drawable.background_night)
            "02d", "04d" -> view.setImageResource(R.drawable.background_few_clouds)
            "03d", "11d" -> view.setImageResource(R.drawable.background_light_rain)
            "09d" -> view.setImageResource(R.drawable.background_heavy_rain)
            "10d" -> view.setImageResource(R.drawable.background_moderate_rain)
            "13d" -> view.setImageResource(R.drawable.background_snow)
            "50d" -> view.setImageResource(R.drawable.background_mist)
            "50n" -> view.setImageResource(R.drawable.background_mist_night)
        }
}

@BindingAdapter("loadingVisibility")
fun loadingVisibility(view: View, status: Status) {
    when (status) {
        null -> {
            view.visibility = View.VISIBLE
        }
        else -> {
            if (status == Status.LOADING)
                view.visibility = View.VISIBLE
            else
                view.visibility = View.GONE
        }
    }
}

@BindingAdapter("gpsVisibility")
fun gpsVisibility(view: View, status: Status) {
    when (status) {
        null -> {
            view.visibility = View.VISIBLE
        }
        else -> {
            if (status == Status.START)
                view.visibility = View.VISIBLE
            else
                view.visibility = View.GONE
        }
    }
}

@BindingAdapter("errorVisibility")
fun errorVisibility(view: TextView, status: Status) {
    when (status) {
        Status.ERROR -> {
            view.visibility = View.VISIBLE
            view.text = "Error occurred"
        }
        Status.NOT_FOUND -> {
            view.visibility = View.VISIBLE
            view.text = "Not Found"
        }
        else -> view.visibility = View.GONE
    }
}

@BindingAdapter("searchListVisibility")
fun searchListVisibility(view: View, status: Status) {
    when (status) {
        Status.SUCCESS -> view.visibility = View.VISIBLE
        else -> view.visibility = View.GONE
    }
}

//Temp
@BindingAdapter("setTempDescription")
fun setTempDescription(view: TextView, desc: String) {
    view.text = desc.capitalize()
}

@BindingAdapter("setTempBg")
fun setTempBg(view: ImageView, temp: Double) {
    val temperature = temp.roundToInt()

    if (Setting.unit == Unit.metric) {
        when {
            temperature <= 25 -> view.setImageResource(R.drawable.background_very_low_warm)
            temperature <= 30 -> view.setImageResource(R.drawable.background_low_warm)
            temperature <= 35 -> view.setImageResource(R.drawable.background_medium_warm)
            temperature <= 90 -> view.setImageResource(R.drawable.background_high_warm)
        }
    } else {
        when {
            temperature <= 77 -> view.setImageResource(R.drawable.background_very_low_warm)
            temperature <= 86 -> view.setImageResource(R.drawable.background_low_warm)
            temperature <= 95 -> view.setImageResource(R.drawable.background_medium_warm)
            temperature <= 194 -> view.setImageResource(R.drawable.background_high_warm)
        }
    }
}

@BindingAdapter("setThermometer")
fun setThermometer(view: ImageView, temp: Double) {
    val temperature = temp.roundToInt()
    when {
        temperature < 0 -> view.setImageResource(R.drawable.c00)
        temperature == 0 -> view.setImageResource(R.drawable.c0)
        temperature <= 5 -> view.setImageResource(R.drawable.c5)
        temperature <= 10 -> view.setImageResource(R.drawable.c10)
        temperature <= 15 -> view.setImageResource(R.drawable.c15)
        temperature <= 20 -> view.setImageResource(R.drawable.c20)
        temperature <= 25 -> view.setImageResource(R.drawable.c25)
        temperature <= 30 -> view.setImageResource(R.drawable.c30)
        temperature <= 35 -> view.setImageResource(R.drawable.c35)
        temperature <= 40 -> view.setImageResource(R.drawable.c40)
        temperature <= 45 -> view.setImageResource(R.drawable.c45)
        temperature <= 50 -> view.setImageResource(R.drawable.c50)
        temperature <= 55 -> view.setImageResource(R.drawable.c55)
        temperature <= 60 -> view.setImageResource(R.drawable.c60)
        temperature <= 65 -> view.setImageResource(R.drawable.c65)
        temperature <= 70 -> view.setImageResource(R.drawable.c70)
        temperature <= 75 -> view.setImageResource(R.drawable.c75)
        temperature <= 80 -> view.setImageResource(R.drawable.c80)
        temperature <= 85 -> view.setImageResource(R.drawable.c85)
        temperature <= 90 -> view.setImageResource(R.drawable.c90)
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setTempTxt")
fun setTempTxt(tempTxt: TextView, temp: Double) {
    if (Setting.unit == Unit.metric)
        tempTxt.text = temp.roundToInt().toString() + 0x00B0.toChar() + "C"
    else
        tempTxt.text = temp.roundToInt().toString() + 0x00B0.toChar() + "F"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setFeelTempTxt")
fun setFeelTempTxt(tempTxt: TextView, temp: Double) {
    if (Setting.unit == Unit.metric)
        tempTxt.text = "Feels like: " + temp.roundToInt().toString() + 0x00B0.toChar() + "C"
    else
        tempTxt.text = "Feels like: " + temp.roundToInt().toString() + 0x00B0.toChar() + "F"
}

//Humidity
@BindingAdapter("setHumidityBg")
fun setHumidityBg(view: ImageView, Humid: Double) {
    val humidity = Humid.roundToInt()
    when {
        humidity <= 25 -> view.setImageResource(R.drawable.background_low_humid)
        humidity <= 65 -> view.setImageResource(R.drawable.background_medium_humid)
        humidity <= 75 -> view.setImageResource(R.drawable.background_high_humid)
        humidity <= 90 -> view.setImageResource(R.drawable.background_very_high_humid)
    }
}

@BindingAdapter("setHumidityMeter")
fun setHumidityMeter(view: ImageView, humid: Double) {
    val humidity = humid.roundToInt()
    when {
        humidity <= 0 -> view.setImageResource(R.drawable.w0)
        humidity <= 5 -> view.setImageResource(R.drawable.w5)
        humidity <= 10 -> view.setImageResource(R.drawable.w10)
        humidity <= 15 -> view.setImageResource(R.drawable.w15)
        humidity <= 20 -> view.setImageResource(R.drawable.w20)
        humidity <= 25 -> view.setImageResource(R.drawable.w25)
        humidity <= 30 -> view.setImageResource(R.drawable.w30)
        humidity <= 35 -> view.setImageResource(R.drawable.w35)
        humidity <= 40 -> view.setImageResource(R.drawable.w40)
        humidity <= 45 -> view.setImageResource(R.drawable.w45)
        humidity <= 50 -> view.setImageResource(R.drawable.w50)
        humidity <= 55 -> view.setImageResource(R.drawable.w55)
        humidity <= 60 -> view.setImageResource(R.drawable.w60)
        humidity <= 65 -> view.setImageResource(R.drawable.w65)
        humidity <= 70 -> view.setImageResource(R.drawable.w70)
        humidity <= 75 -> view.setImageResource(R.drawable.w75)
        humidity <= 80 -> view.setImageResource(R.drawable.w80)
        humidity <= 85 -> view.setImageResource(R.drawable.w85)
        humidity <= 90 -> view.setImageResource(R.drawable.w90)
    }
}

@BindingAdapter("setHumidityTxt")
fun setHumidityTxt(tempTxt: TextView, temp: Double) {
    tempTxt.text = temp.roundToInt().toString() + "%"
}

//Pollution
@BindingAdapter("setAirQualityTextColor")
fun setAirQualityTextColor(textView: TextView, aqi: Int) {
    when (aqi) {
        1 -> {
//            textView.setTextColor(Color.GREEN)
            textView.text = "Good"
        }
        2 -> {
//            textView.setTextColor(Color.parseColor("#ACFA58"))
            textView.text = "Fair"
        }
        3 -> {
//            textView.setTextColor(Color.parseColor("#F3F781"))
            textView.text = "Moderate"
        }
        4 -> {
//            textView.setTextColor(Color.parseColor("#D8D8D8"))
            textView.text = "Poor"
        }
        5 -> {
//            textView.setTextColor(Color.parseColor("#848484"))
            textView.text = "Very poor"
        }
    }
}

@BindingAdapter("setPollutionBg")
fun setPollutionBg(view: LinearLayout, type: Int) {
    when (type) {
        1 -> view.setBackgroundResource(R.drawable.back_good)
        2 -> view.setBackgroundResource(R.drawable.back_fair)
        3 -> view.setBackgroundResource(R.drawable.back_moderate)
        4 -> view.setBackgroundResource(R.drawable.back_poor)
        5 -> view.setBackgroundResource(R.drawable.back_very_poor)
    }
}

//Wind
@BindingAdapter("setWindTxt")
fun setWindTxt(windTxt: TextView, speed: Double) {
    if (Setting.unit == Unit.metric)
        windTxt.text = "$speed meter/s"
    else
        windTxt.text = "$speed miles/h"

}

@BindingAdapter("setWindDeg")
fun setWindDeg(windDeg: TextView, deg: Double) {
    windDeg.text = getWindDirection(deg)
}

@BindingAdapter("setArrowImg")
fun setArrowImg(arrow: ImageView, deg: Double) {
    arrow.rotation = deg.toFloat()
}

@BindingAdapter("setTurbineAnimation")
fun setTurbineAnimation(turbine: LottieAnimationView, speed: Double) {
    if (Setting.unit == Unit.metric)
        turbine.speed = speed.toFloat()
    else
        turbine.speed = (speed / 2.24).toFloat()
}

@BindingAdapter("setWindDirectionAnim")
fun setWindDirectionAnim(turbine: LottieAnimationView, deg: Double) {
    if (deg in 180.0..360.0) {
        turbine.reverseAnimationSpeed()
    }
}

@BindingAdapter("setLinesAnimation")
fun setLinesAnimation(lines: LottieAnimationView, speed: Double) {
    if (Setting.unit == Unit.metric)
        lines.speed = (speed / 3).toFloat()
    else
        lines.speed = (speed / 6.72).toFloat()
}

//Pressure
@BindingAdapter("setPressureImg")
fun setPressureImg(barometerImg: ImageView, pressure: Double) {
    when {
        pressure <= 960 -> barometerImg.setImageResource(R.drawable.p0)
        pressure > 960 && pressure <= 970 -> barometerImg.setImageResource(R.drawable.p0)
        pressure > 970 && pressure <= 980 -> barometerImg.setImageResource(R.drawable.p10)
        pressure > 980 && pressure <= 990 -> barometerImg.setImageResource(R.drawable.p20)
        pressure > 990 && pressure <= 1000 -> barometerImg.setImageResource(R.drawable.p30)
        pressure > 1000 && pressure <= 1010 -> barometerImg.setImageResource(R.drawable.p40)
        pressure > 1010 && pressure <= 1020 -> barometerImg.setImageResource(R.drawable.p50)
        pressure > 1020 && pressure <= 1030 -> barometerImg.setImageResource(R.drawable.p60)
        pressure > 1030 && pressure <= 1040 -> barometerImg.setImageResource(R.drawable.p70)
        pressure > 1040 && pressure <= 1050 -> barometerImg.setImageResource(R.drawable.p80)
        pressure > 1050 -> barometerImg.setImageResource(R.drawable.p90)
    }
}

@BindingAdapter("setSunriseSunsetTxt")
fun setSunriseSunsetTxt(sunrise: TextView, time: Long) {
    sunrise.text = unixToTime(time)
}

@BindingAdapter("setTextColor")
fun setTextColor(textView: TextView, icon: String) {
    when {
        icon.endsWith("n") -> {
            textView.setTextColor(ContextCompat.getColor(textView.context, R.color.silver_blue))
        }
        else -> textView.setTextColor(ContextCompat.getColor(textView.context, R.color.black))
    }
}

@BindingAdapter("setDayText")
fun setDayText(textView: TextView, date: String) {
    textView.text = getDayOfWeek(date)
}

@BindingAdapter("setTimeText")
fun setTimeText(textView: TextView, time: String) {
    textView.text = time.substring(11, 16)
}

@BindingAdapter("setFlagImg")
fun setFlagImg(imageView: ImageView, countryCode: String) {
    val drawableId: Int =
        imageView.resources.getIdentifier(
            countryCode.toLowerCase(),
            "drawable",
            imageView.context.packageName
        )
    imageView.setImageResource(drawableId)
}

fun getWindDirection(d: Double): String? {
    var direction: String? = ""
    val deg = d.roundToInt()

    if (deg > 348.75 || deg >= 0 && deg <= 11.25) direction =
        "North" else if (deg > 11.25 && deg <= 33.75) direction =
        "North-NorthEast" else if (deg > 33.75 && deg <= 56.25) direction =
        "North East" else if (deg > 56.25 && deg < 78.75) direction =
        "East-NorthEast" else if (deg >= 78.75 && deg <= 101.25) direction =
        "East" else if (deg > 101.25 && deg <= 123.75) direction =
        "East-SouthEast" else if (deg > 123.75 && deg <= 146.25) direction =
        "SouthEast" else if (deg > 146.25 && deg < 168.75) direction =
        "South-SouthEast" else if (deg >= 168.75 && deg <= 191.25) direction =
        "South" else if (deg > 191.25 && deg <= 213.75) direction =
        "South-SouthWest" else if (deg > 213.75 && deg <= 236.25) direction =
        "SouthWest" else if (deg > 236.25 && deg <= 258.75) direction =
        "West-SouthWest" else if (deg > 258.75 && deg <= 281.25) direction =
        "West" else if (deg > 281.25 && deg <= 303.75) direction =
        "West-NorthWest" else if (deg > 303.75 && deg <= 326.25) direction =
        "NorthWest" else if (deg > 326.25 && deg <= 348.75) direction =
        "North-NorthWest"
    return direction
}

private fun unixToTime(unixSeconds: Long): String? {
    val calendar = Calendar.getInstance(
        TimeZone.getTimeZone("GMT"),
        Locale.getDefault()
    )
    val currentLocalTime = calendar.time
    val date2: DateFormat = SimpleDateFormat("ZZZZZ", Locale.getDefault())
    val localTime = date2.format(currentLocalTime)
    val date = Date(unixSeconds * 1000L)
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")
    sdf.timeZone = TimeZone.getTimeZone("GMT$localTime")
    val formattedDate = sdf.format(date)
    return formattedDate.substring(10, 16)
}

fun getDayOfWeek(str: String?): String? {
    var date1: Date? = Date()
    try {
        date1 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(str)
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
    return dayOfWeek
}
