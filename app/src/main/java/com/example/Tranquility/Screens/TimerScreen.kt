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
import com.example.Tranquility.Models.MeditationLog
import com.example.Tranquility.ViewModels.MeditationLogViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerWithProgressBar(viewModel: MeditationLogViewModel) {
    // State to track elapsed time, timer status, and selected time
    var timeElapsed by remember { mutableIntStateOf(600) }
    var isRunning by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(10) } // Default to 10 minutes
    val totalTimeInSeconds = selectedTime * 60 // Convert selected time to seconds
    val progress = (totalTimeInSeconds - timeElapsed) / totalTimeInSeconds.toFloat()
    var showTimePicker by remember { mutableStateOf(false) }

    // Timer logic that updates every second if running
    LaunchedEffect(isRunning) {
        while (isRunning && timeElapsed > 0) {
            delay(1000) // Wait 1 second
            timeElapsed -= 1 // Decrease the elapsed time
        }
        // Save meditation log when timer reaches zero
        if (timeElapsed == 0) {
            saveTimerData(viewModel, selectedTime)
        }
    }

    // UI for the timer screen
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Meditation Timer", style = TextStyle(fontSize = 20.sp)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = Color(0xFFA7D8DE) // Background color for the screen
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Circular progress indicator for the timer
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, shape = CircleShape)
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .size(180.dp)
                        .padding(10.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 8.dp,
                )
                // Display time remaining
                Text(
                    text = String.format("%02d:%02d", timeElapsed / 60, timeElapsed % 60),
                    style = TextStyle(fontSize = 32.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Display message when time is up
            if (timeElapsed == 0) {
                Text(
                    text = "Meditation Saved!",
                    style = TextStyle(fontSize = 20.sp, color = MaterialTheme.colorScheme.error)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Button to select meditation time
            Button(
                onClick = { showTimePicker = true },
                modifier = Modifier.fillMaxWidth(0.7f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text(text = "Select Time", style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onTertiary))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to start/stop the timer
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

            // Show custom time picker dialog when the user selects time
            if (showTimePicker) {
                CustomTimePickerDialog(
                    onDismiss = { showTimePicker = false },
                    onTimeSelected = { hour, minute ->
                        selectedTime = hour * 60 + minute // Update selected time
                        timeElapsed = selectedTime * 60 // Reset timer
                        showTimePicker = false
                    }
                )
            }
        }
    }
}

// Function to save meditation data using the ViewModel
private fun saveTimerData(viewModel: MeditationLogViewModel, meditationTime: Int) {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedDateTime = currentDateTime.format(formatter)

    // Create a MeditationLog object and save it using the ViewModel
    val meditationLog = MeditationLog(
        dateTime = formattedDateTime,
        meditationTime = meditationTime
    )

    viewModel.addMeditationLog(meditationLog)
}

// Custom dialog for selecting time
@Composable
fun CustomTimePickerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    // State to track selected hour and minute
    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(10) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Select Time")
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Slider for selecting hours
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Hours: $hour", style = MaterialTheme.typography.bodySmall)
                        Slider(
                            value = hour.toFloat(),
                            onValueChange = { hour = it.toInt() },
                            valueRange = 0f..23f,
                            steps = 23
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Slider for selecting minutes
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Minutes: $minute", style = MaterialTheme.typography.bodySmall)
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


