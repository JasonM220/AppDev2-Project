package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TimerWithProgressBar() {
    var timeElapsed by remember { mutableStateOf(60) }
    val totalTime = 60
    val progress = (totalTime - timeElapsed) / totalTime.toFloat()

    LaunchedEffect(timeElapsed) {
        if (timeElapsed > 0) {
            delay(1000)
            timeElapsed -= 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = String.format("%02d:%02d", timeElapsed / 60, timeElapsed % 60),
            style = TextStyle(fontSize = 48.sp, color = Color.Black)
        )

        Spacer(modifier = Modifier.height(32.dp))

        LinearProgressIndicator(
            progress = {
                progress
            },
            modifier = Modifier.fillMaxWidth().height(10.dp),
            color = MaterialTheme.colorScheme.primary,
        )

        if (timeElapsed == 0) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Time's Up!",
                style = TextStyle(fontSize = 24.sp, color = Color.Red)
            )
        }
    }
}
