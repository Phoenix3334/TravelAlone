package com.example.travelalone.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val status: String,
    val count: String,
    val info: String,
    val infocode: String,
    val forecasts: List<Forecast>
)

data class Forecast (
    val city: String,
    val adcode: String,
    val province: String,
    val reporttime: String,
    val casts: List<WeatherCast>
)

data class WeatherCast(
    val date: String,
    val week: String,
    val dayweather: String,
    val nightweather: String,
    val daytemp: String,
    val nighttemp: String,
    val daywind: String,
    val nightwind: String,
    val daypower: String,
    val nightpower: String,
    @SerializedName("daytemp_float") val daytempFloat: String,
    @SerializedName("nighttemp_float") val nighttempFloat: String
)
