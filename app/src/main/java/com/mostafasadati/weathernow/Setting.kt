package com.mostafasadati.weathernow

import com.mostafasadati.weathernow.widgets.WidgetColor
import java.util.*

class Setting {
    companion object {
        var SHOULD_UPDATE = true
        var lat: Float = 51.5085f
        var lon: Float = -0.1257f
        var unit: Unit = Unit.Metric
        var audio: Boolean = true
        var currentWidgetColor: WidgetColor = WidgetColor.Light
        var forecastWidgetColor: WidgetColor = WidgetColor.Light
        var lastUpdate: Long = 0
        var locale: Locale = Locale.US
    }
}