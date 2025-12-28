package com.example.travelalone.network

import com.example.travelalone.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v3/weather/weatherInfo")
    fun getWeather(
        @Query("key") apiKey: String,
        @Query("city") city: String,
        @Query("extensions") extensions: String = "all",
        @Query("output") output: String = "json"
    ): Call<WeatherResponse>
}

