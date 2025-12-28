package com.example.travelalone.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.layout.ContentScale
import com.example.travelalone.viewmodel.NewsViewModel

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val context = LocalContext.current
    val newsList by viewModel.newsList.observeAsState(emptyList())
    val error by viewModel.error.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(newsList) { news ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // 点击 Card 打开浏览器
                            news.link?.takeIf { it.isNotBlank() }?.let { url ->
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        // 图片
                        news.img?.takeIf { it.isNotBlank() }?.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // 标题
                        Text(
                            text = news.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // 来源 + 时间
                        Text(
                            text = "${news.source} · ${news.pubDate}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // 简介
                        news.desc?.takeIf { it.isNotBlank() }?.let { desc ->
                            Text(
                                text = desc,
                                fontSize = 14.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
