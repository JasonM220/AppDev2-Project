package com.example.Tranquility

import LoginScreen
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.Tranquility.Navigation.Screen
import com.example.Tranquility.Navigation.TopNavBar
import com.example.Tranquility.Screens.RegisterScreen
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
                AppNavHost()

            }

        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun AppNavHost() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Screen.Login.route) {

            composable(Screen.Login.route) {
                LoginScreen(
                    onLogin = {navController.navigate(Screen.Timer.route)},
                    onRegisterClick = { navController.navigate(Screen.Register.route) }, // Navigate to About screen
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    returnToLogin = { navController.navigate(Screen.Login.route) }
                )
            }

            composable(Screen.Timer.route) {
                Scaffold(
                    topBar = {
                        TopNavBar(navController = navController, onLogout = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        })
                    }
                ) {
                    TimerWithProgressBar()
                }
            }

            composable(Screen.Log.route) {
                Scaffold(
                    topBar = {
                        TopNavBar(navController = navController, onLogout = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        })
                    }
                ) {
                    Text("Log Screen Content")
                }
            }
            composable(Screen.Chat.route) {
                Scaffold(
                    topBar = {
                        TopNavBar(navController = navController, onLogout = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        })
                    }
                ) {
                    Text("Chat Screen Content")
                }
            }
        }
    }

}
