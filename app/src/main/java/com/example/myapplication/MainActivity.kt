package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.Screens.ChatbotScreen
import com.example.myapplication.Screens.TimerWithProgressBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //TimerWithProgressBar()
            ChatbotScreen()
        }
    }
}
