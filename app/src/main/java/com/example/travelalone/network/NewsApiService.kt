package com.example.travelalone.network

import com.example.travelalone.model.NewsResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface NewsApiService {

    @FormUrlEncoded
    @POST("109-35")
    fun getNews(
        @Field("appKey") appKey: String,
        @Field("title") title: String
    ): Call<NewsResponse>
}
