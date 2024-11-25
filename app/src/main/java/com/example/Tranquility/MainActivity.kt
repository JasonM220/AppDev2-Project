package com.example.Tranquility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.Tranquility.Screens.TimerWithProgressBar
import com.example.Tranquility.ui.theme.MyApplicationTheme
import  com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initializing our database
        FirebaseApp.initializeApp(this)


        setContent {
            MyApplicationTheme {


            }

        }
    }
}
