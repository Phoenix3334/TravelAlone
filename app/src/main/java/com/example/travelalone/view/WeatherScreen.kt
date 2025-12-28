package com.example.travelalone.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Air
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.travelalone.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weatherDataList by viewModel.weatherDataList.observeAsState(emptyList())
    val error by viewModel.error.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // 确保背景为白色
            .padding(horizontal = 16.dp, vertical = 8.dp) // 调整padding
    ) {

        // ===== 错误提示 =====
        error?.let {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent // 设置为透明，避免粉色
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.background(Color.White) // 确保LazyColumn背景为白色
        ) {
            items(
                weatherDataList.flatMap { response ->
                    response.forecasts.flatMap { forecast ->
                        forecast.casts.map { cast ->
                            Triple(forecast, cast, forecast.reporttime)
                        }
                    }
                }.sortedByDescending { it.third }
            ) { (forecast, cast, _) ->

                Card(
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // ===== 城市 + 日期 =====
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${forecast.city} · ${forecast.province}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = cast.date,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // ===== 天气情况 =====
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            // 白天
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Filled.WbSunny,
                                    contentDescription = null,
                                    tint = Color(0xFFFFA000),
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(cast.dayweather)
                                Text(
                                    text = "${cast.daytemp}°C",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            // 夜晚
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Filled.NightsStay,
                                    contentDescription = null,
                                    tint = Color(0xFF5C6BC0),
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(cast.nightweather)
                                Text(
                                    text = "${cast.nighttemp}°C",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider()

                        Spacer(modifier = Modifier.height(12.dp))

                        // ===== 风力信息 =====
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Air,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("白天：${cast.daywind} ${cast.daypower}级")
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Air,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("夜晚：${cast.nightwind} ${cast.nightpower}级")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "更新时间：${forecast.reporttime}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
