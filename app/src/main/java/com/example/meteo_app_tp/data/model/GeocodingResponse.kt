package com.example.meteo_app_tp.data.model

import com.google.gson.annotations.SerializedName

data class NominatimResponse(
    @SerializedName("display_name") val displayName: String,
    val address: Address
)

data class Address(
    val city: String? = null,
    val town: String? = null,
    val village: String? = null,
    val municipality: String? = null,
    val county: String? = null
)