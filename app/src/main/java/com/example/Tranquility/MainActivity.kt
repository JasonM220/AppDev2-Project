package com.example.Tranquility

import LoginScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.Tranquility.Navigation.Screen
import com.example.Tranquility.Repositories.MeditationLogRepository
import com.example.Tranquility.Screens.MeditationLogScreen
import com.example.Tranquility.Screens.RegisterScreen
import com.example.Tranquility.Screens.TimerWithProgressBar
import com.example.Tranquility.ViewModels.MeditationLogViewModel
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

        val meditationRepo = MeditationLogRepository()
        val meditationViewModel = MeditationLogViewModel(meditationRepo)

        setContent {
            MyApplicationTheme {
                AppNavHost(meditationViewModel)

            }

        }
    }

    // QuizAppNavHost manages the navigation between Quiz, Result, and About screens
    @Composable
    fun AppNavHost(meditationViewModel: MeditationLogViewModel) {
        // Initialize NavController for navigation between screens
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
                TimerWithProgressBar()
            }

            composable(Screen.Log.route) {
                MeditationLogScreen(meditationViewModel)
            }
            composable(Screen.Chat.route) {
                //merge proof-of-concept i.e just copy paste
            }
        }
    }

}
