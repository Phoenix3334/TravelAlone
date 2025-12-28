package com.example.travelalone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelalone.model.NewsItem
import com.example.travelalone.network.NewsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsViewModel : ViewModel() {

    private val _newsList = MutableLiveData<List<NewsItem>>(emptyList())
    val newsList: LiveData<List<NewsItem>> = _newsList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://route.showapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(NewsApiService::class.java)

    fun fetchNews(city: String) {
        // 清空旧数据
        _newsList.postValue(emptyList())
        _error.postValue("")

        api.getNews(
            appKey = "33290918Afe14D12BD507C9A76699dC6",
            title = city
        ).enqueue(object : retrofit2.Callback<com.example.travelalone.model.NewsResponse> {

            override fun onResponse(
                call: retrofit2.Call<com.example.travelalone.model.NewsResponse>,
                response: retrofit2.Response<com.example.travelalone.model.NewsResponse>
            ) {
                val list = response.body()
                    ?.showapi_res_body
                    ?.pagebean
                    ?.contentlist

                if (list != null) {
                    _newsList.postValue(list)
                } else {
                    _error.postValue("没有查到新闻")
                }
            }

            override fun onFailure(call: retrofit2.Call<com.example.travelalone.model.NewsResponse>, t: Throwable) {
                _error.postValue(t.message)
            }
        })
    }
}
