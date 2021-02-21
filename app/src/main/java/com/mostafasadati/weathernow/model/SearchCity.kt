package com.mostafasadati.weathernow.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchCity(
    val country: String,
    val lat: Float,
    val lon: Float,
    val name: String
) : Parcelable