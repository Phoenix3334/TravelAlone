package com.example.travelalone.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelalone.viewmodel.ChatViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.LaunchedEffect
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.html.HtmlRenderer
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import com.example.travelalone.model.ChatMessage

@Composable
fun ChatScreen(chatViewModel: ChatViewModel) {

    val messages by chatViewModel.messages.observeAsState(emptyList())
    val isLoading by chatViewModel.isLoading.observeAsState(false)
    val error by chatViewModel.error.observeAsState()

    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {

        // 消息列表
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages.filter { it.role != "system" }) { message ->
                MessageBubble(message)
            }
        }

        // 自动滚动到底部
        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }

        // 错误提示
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        // 输入框和发送按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("请输入你的问题") },
                maxLines = 4
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (input.isNotBlank()) {
                        chatViewModel.sendMessage(input)
                        input = ""
                    }
                },
                enabled = !isLoading
            ) {
                Text(if (isLoading) "思考中..." else "发送")
            }
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isUser = message.role == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isUser) Color(0xFF90CAF9) else Color(0xFFE0E0E0),
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 4.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            if (isUser) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp),
                    color = Color.White
                )
            } else {
                // AI 消息支持 Markdown 渲染
                MarkdownWebView(message.content)
            }
        }
    }
}

@Composable
fun MarkdownWebView(markdownText: String) {
    val htmlText = remember(markdownText) {
        val parser = Parser.builder().build()
        val document: Node = parser.parse(markdownText)
        HtmlRenderer.builder().build().render(document)
    }

    AndroidView(factory = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = false
            loadDataWithBaseURL(null, htmlText, "text/html", "utf-8", null)
            setBackgroundColor(0x00000000) // 透明背景
        }
    }, modifier = Modifier.padding(8.dp))
}
