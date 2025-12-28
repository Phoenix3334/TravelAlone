package com.example.travelalone.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Chat
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.ai.Chat

/**
 * 底部导航项定义
 */
sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Home : BottomNavItem(
        route = "home",
        icon = Icons.Default.Home,
        title = "首页"
    )

    object WeatherList : BottomNavItem(
        route = "weather_list",
        icon = Icons.Default.List,
        title = "天气预报"
    )
    object NewsList : BottomNavItem(
        route = "news_list",
        icon = Icons.Default.List,
        title = "新闻列表"
    )
    object Chat : BottomNavItem(
        route = "chat",
        title = "对话",
        icon = Icons.Default.Chat
    )

}

/**
 * 获取所有底部导航项
 */
fun getAllBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem.Home,
        BottomNavItem.WeatherList,
        BottomNavItem.NewsList,
        BottomNavItem.Chat
    )
}
