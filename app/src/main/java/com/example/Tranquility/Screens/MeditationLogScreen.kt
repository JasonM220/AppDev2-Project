package com.example.Tranquility.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Tranquility.Models.MeditationLog
import com.example.Tranquility.ViewModels.MeditationLogViewModel

@Composable
fun MeditationLogScreen(viewModel: MeditationLogViewModel) {
    val meditationLogs by viewModel.meditationLogs.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF1F8)) //background color for now lil ugly...
            .padding(16.dp)
    ) {
        //title
        Text(
            text = "Your Meditations:",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        //lazy column scroll for meditations
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(meditationLogs) { log ->
                MeditationLogItem(log, onDelete = { viewModel.deleteMeditationLog(log) })
            }
        }
    }
}



@Composable //each specific log within the list
fun MeditationLogItem(log: MeditationLog, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //The Meditation image?>....
        Text(
            text = "M",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFE1BEE7), shape = MaterialTheme.shapes.small)
                .wrapContentSize(Alignment.Center)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Meditation: ${log.dateTime},  ",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${log.meditationTime} min",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp
            )
        }

        //delete button
        Text(
            text = "❌",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Red,
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable {
                    Log.d("MeditationLogItem", "Delete clicked for: ${log.dateTime}")
                    onDelete()
                }
        )
    }
}



