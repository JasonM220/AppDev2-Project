package com.example.Tranquility.Screens

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerWithProgressBar() {
    var timeElapsed by remember { mutableStateOf(600) }
    var isRunning by remember { mutableStateOf(false) }

    var selectedTime by remember { mutableStateOf(10) }
    val totalTimeInSeconds = selectedTime * 60
    val progress = (totalTimeInSeconds - timeElapsed) / totalTimeInSeconds.toFloat()

    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(isRunning) {
        while (isRunning && timeElapsed > 0) {
            delay(1000)
            timeElapsed -= 1
        }
        if (timeElapsed == 0) {
            saveTimerData(selectedTime)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Meditation Timer", style = TextStyle(fontSize = 20.sp))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = Color(0xFF40E0D0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Circular Progress Bar
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, shape = CircleShape)
            ) {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .size(180.dp)
                        .padding(10.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 8.dp
                )
                Text(
                    text = String.format("%02d:%02d", timeElapsed / 60, timeElapsed % 60),
                    style = TextStyle(fontSize = 32.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (timeElapsed == 0) {
                Text(
                    text = "Time's Up!",
                    style = TextStyle(fontSize = 20.sp, color = MaterialTheme.colorScheme.error)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { showTimePicker = true },
                modifier = Modifier.fillMaxWidth(0.7f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(
                    text = "Select Time",
                    style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onTertiary)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { isRunning = !isRunning },
                modifier = Modifier.fillMaxWidth(0.7f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (isRunning) "Stop Timer" else "Start Timer",
                    style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
                )
            }

            // Show custom time picker dialog
            if (showTimePicker) {
                CustomTimePickerDialog(
                    onDismiss = { showTimePicker = false },
                    onTimeSelected = { hour, minute ->
                        selectedTime = hour * 60 + minute
                        timeElapsed = selectedTime * 60
                        showTimePicker = false
                    }
                )
            }
        }
    }
}

@Composable
fun CustomTimePickerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(10) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Select Time")
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Time Pickers
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Hour Picker
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Hours: $hour", style = MaterialTheme.typography.bodySmall) // Show the selected hours
                        Slider(
                            value = hour.toFloat(),
                            onValueChange = { hour = it.toInt() },
                            valueRange = 0f..23f,
                            steps = 23
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Minute Picker
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Minutes: $minute", style = MaterialTheme.typography.bodySmall) // Show the selected minutes
                        Slider(
                            value = minute.toFloat(),
                            onValueChange = { minute = it.toInt() },
                            valueRange = 0f..59f,
                            steps = 59
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onTimeSelected(hour, minute) }) {
                Text("Set Time")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}



// Save Timer Data Function
private fun saveTimerData(meditationTime: Int) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    val user = auth.currentUser
    if (user != null) {
        val uid = user.uid
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        val meditationData = hashMapOf(
            "userId" to uid,
            "dateTime" to formattedDateTime,
            "meditationTime" to meditationTime
        )

        firestore.collection("meditationSessions")
            .add(meditationData)
            .addOnSuccessListener {
                println("Meditation data successfully saved!")
            }
            .addOnFailureListener { e ->
                println("Error saving meditation data: $e")
            }
    }
}
