package com.mostafasadati.weathernow.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchCity(
    val country: String,
    val lat: Float,
    val lon: Float,
    val local_names: LocalNames?,
    val name: String
) : Parcelable {
    @Parcelize
    data class LocalNames(
        val feature_name: String?
    ) : Parcelable
}
