package com.mostafasadati.weathernow

class Setting {
    companion object {
        var city: String = "Tehran"
        var country: String = "IR"
        var lat: Float = 35.6944f
        var lon: Float = 51.4215f
        var unit: Unit = Unit.metric
        var audio: Boolean = true
        var widgetColor: WidgetColor = WidgetColor.light
        var lastUpdate: Long = 0
    }
}