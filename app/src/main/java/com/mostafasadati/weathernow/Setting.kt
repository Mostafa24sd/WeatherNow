package com.mostafasadati.weathernow

import com.mostafasadati.weathernow.widgets.WidgetColor
import java.util.*

class Setting {
    companion object {
        var SHOULD_UPDATE = true
        var city: String = "Tehran"
        var country: String = "IR"
        var lat: Float = 35.6944f
        var lon: Float = 51.4215f
        var unit: Unit = Unit.Metric
        var audio: Boolean = true
        var currentWidgetColor: WidgetColor = WidgetColor.Light
        var forecastWidgetColor: WidgetColor = WidgetColor.Light
        var lastUpdate: Long = 0
        var locale: Locale = Locale.US
    }
}