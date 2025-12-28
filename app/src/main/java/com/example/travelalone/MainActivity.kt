package com.example.travelalone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.travelalone.view.ChatScreen
import com.example.travelalone.view.NewsScreen
import com.example.travelalone.view.WeatherScreen
import com.example.travelalone.viewmodel.BottomNavItem
import com.example.travelalone.viewmodel.ChatViewModel
import com.example.travelalone.viewmodel.NewsViewModel
import com.example.travelalone.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        setContent {
            MainScreen(weatherViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(weatherViewModel: WeatherViewModel) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {},
        bottomBar = {
            BottomAppBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.WeatherList,
                    BottomNavItem.NewsList,
                    BottomNavItem.Chat
                )
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // 设置背景颜色为主题背景
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = BottomNavItem.Home.route,
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // 确保内容区域背景一致
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen(weatherViewModel) }
            composable(BottomNavItem.WeatherList.route) { WeatherScreen(weatherViewModel) }
            composable(BottomNavItem.NewsList.route) {
                val newsViewModel: NewsViewModel = viewModel()
                val city by weatherViewModel.currentCity.observeAsState()
                city?.let {
                    newsViewModel.fetchNews(it)
                }
                NewsScreen(newsViewModel)
            }
            composable(BottomNavItem.Chat.route) {
                val chatViewModel: ChatViewModel = viewModel()
                ChatScreen(chatViewModel)
            }

        }
    }
}

@Composable
fun HomeScreen(weatherViewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    val queryStatus by weatherViewModel.queryStatus.observeAsState()

    DisposableEffect(Unit) {
        onDispose {
            weatherViewModel.clearQueryStatus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(48.dp))

        // ===== 标题区域 =====
        Text(
            text = "City Mate",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "探索一座城市，从这里开始",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        // ===== 输入卡片 =====
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "城市查询",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("请输入城市名") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (city.isNotBlank()) {
                            weatherViewModel.fetchWeather(city)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("查询城市信息")
                }
            }
        }

        // ===== 状态反馈 =====
        queryStatus?.let { status ->
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (status.contains("成功"))
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = status,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = if (status.contains("成功"))
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}
