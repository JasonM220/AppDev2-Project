package com.example.Tranquility.Navigation


sealed class Screen(val route: String) {
    object Register : Screen("register_screen")
    object Login : Screen("login_screen")
    object Timer : Screen("timer_screen")
    object Chat : Screen("chat_screen")
    object Log : Screen("log_screen")


}