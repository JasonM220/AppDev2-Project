package com.example.myapplication.Screens

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@SuppressLint("DefaultLocale")
@Composable
fun TimerWithProgressBar() {
    var timeElapsed by remember { mutableStateOf(600) }
    var isRunning by remember { mutableStateOf(false) }

    var selectedTime by remember { mutableStateOf(10) }
    val totalTimeInSeconds = selectedTime * 60

    val progress = (totalTimeInSeconds - timeElapsed) / totalTimeInSeconds.toFloat()

    val context = LocalContext.current

    LaunchedEffect(isRunning) {
        while (isRunning && timeElapsed > 0) {
            delay(1000)
            timeElapsed -= 1
        }
    }

    val timePickerListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, minute: Int ->
        selectedTime = (hourOfDay * 60) + minute
        timeElapsed = selectedTime * 60
    }

    val showTimePicker = {
        TimePickerDialog(
            context,
            timePickerListener,
            0,
            10,
            true
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(32.dp))


        Text(
            text = String.format("%02d:%02d", timeElapsed / 60, timeElapsed % 60),
            style = TextStyle(fontSize = 48.sp, color = Color.Black)
        )

        if (timeElapsed == 0) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Time's Up!",
                style = TextStyle(fontSize = 24.sp, color = Color.Red)
            )
        }

        Spacer(modifier = Modifier.height(200.dp))

        Button(
            onClick = showTimePicker
        ) {
            Text(
                text = "Select Time",
                style = TextStyle(fontSize = 18.sp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { isRunning = !isRunning }
        ) {
            Text(
                text = if (isRunning) "Stop Timer" else "Start Timer",
                style = TextStyle(fontSize = 18.sp)
            )
        }
    }
}
