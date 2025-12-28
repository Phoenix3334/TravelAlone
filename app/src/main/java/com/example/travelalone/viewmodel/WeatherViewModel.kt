package com.example.travelalone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.example.travelalone.model.WeatherResponse
import com.example.travelalone.network.WeatherApiService

class WeatherViewModel : ViewModel() {

    private val _currentCity = MutableLiveData<String>()
    val currentCity: LiveData<String> = _currentCity

    private val _weatherDataList = MutableLiveData<List<WeatherResponse>>(emptyList())
    val weatherDataList: LiveData<List<WeatherResponse>> = _weatherDataList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _queryStatus = MutableLiveData<String?>()
    var queryStatus: LiveData<String?> = _queryStatus

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://restapi.amap.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherApiService = retrofit.create(WeatherApiService::class.java)

    fun fetchWeather(city: String) {
        val apiKey = "4450c5d127bf07cb2c16c8167cd99980"
        // 清空之前的状态
        _weatherDataList.postValue(emptyList())
        _queryStatus.postValue(null)
        _error.postValue("")
        _currentCity.value = city

        weatherApiService.getWeather(apiKey, city).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.forecasts.isNotEmpty()) {
                            val currentList = _weatherDataList.value ?: emptyList()
                            _weatherDataList.postValue(currentList + it)
                            _queryStatus.postValue("城市信息查询成功！")
                        } else {
                            _queryStatus.postValue("查询失败，未找到相关天气信息")
                        }
                    } ?: run {
                        _queryStatus.postValue("查询失败，返回数据为空")
                    }
                } else {
                    _error.postValue("Error: ${response.code()}")
                    _queryStatus.postValue("查询失败，请检查城市名称")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                _error.postValue("Failure: ${t.message}")
                _queryStatus.postValue("查询失败，网络连接异常")
            }
        })
    }

    fun clearQueryStatus() {
        _queryStatus.postValue(null)
        queryStatus = _queryStatus
    }
}
