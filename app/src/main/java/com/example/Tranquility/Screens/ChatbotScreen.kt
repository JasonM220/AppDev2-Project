package com.example.Tranquility.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.Tranquility.viewmodels.ChatbotViewModel

@Composable
fun ChatbotScreen(viewModel: ChatbotViewModel = viewModel()) {

    var userInput by remember { mutableStateOf("") }

    //container for whole screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // display chat history in a scrollable type list (lazy column)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //iterate over chat messages to display
            items(viewModel.chatMessages) { (sender, message) ->
                Text(
                    text = "$sender: $message",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
        }

        // row with the input box and send button.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                placeholder = { Text("Ask me anything...") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (userInput.isNotBlank()) {
                        viewModel.sendMessage(userInput)
                        userInput = ""
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}
