package com.example.travelalone.model

data class NewsResponse(
    val showapi_res_code: Int,
    val showapi_res_body: NewsBody
)

data class NewsBody(
    val pagebean: PageBean
)

data class PageBean(
    val contentlist: List<NewsItem>
)

data class NewsItem(
    val title: String,
    val source: String,
    val pubDate: String,
    val link: String,
    val desc: String = "", // 添加描述字段，默认为空字符串
    val img: String = ""   // 添加图片字段，默认为空字符串
)
