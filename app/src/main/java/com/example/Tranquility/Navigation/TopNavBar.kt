package com.example.Tranquility.Navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.Tranquility.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(navController: NavController, onLogout: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Tranquility App" // Display username if available or "Tranquility App"

    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = userName)
            }
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.smoothed_meditation_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(50.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = androidx.compose.ui.graphics.Color(0xFFE5DFF6),
            titleContentColor = androidx.compose.ui.graphics.Color.Black,
            navigationIconContentColor = androidx.compose.ui.graphics.Color.Black
        ),
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        navController.navigate(Screen.Timer.route)
                    },
                    text = { Text("Timer") }
                )
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        navController.navigate(Screen.Log.route)
                    },
                    text = { Text("Log") }
                )
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        navController.navigate(Screen.Chat.route)
                    },
                    text = { Text("Chat") }
                )
                HorizontalDivider()
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onLogout()
                    },
                    text = { Text("Log Out") }
                )
            }
        }
    )
}
