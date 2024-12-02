package com.example.Tranquility.Models

data class MeditationLog(
    var dateTime: String = "",
    var meditationTime: Int = 0,
    var userId: String = ""         //filtering by user
)
