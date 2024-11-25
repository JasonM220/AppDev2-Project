package com.example.Tranquility

import LoginScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.Tranquility.Screens.TimerWithProgressBar
import com.example.Tranquility.ui.theme.MyApplicationTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            FirebaseApp.initializeApp(this)
            Log.d("FirebaseInit", "Firebase successfully initialized")
        } catch (e: Exception) {
            Log.e("FirebaseInit", "Firebase initialization failed: ${e.message}")
        }
        setContent {
            MyApplicationTheme {
                LoginScreen()

            }

        }
    }
}
