package com.pashteut.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity serves as the entry point for the ToDo application.
 *
 * This activity sets up the main theme of the application and initializes the navigation components.
 * It defines the navigation graph for the application, specifying the start destination and the composable screens for main, detail, and authentication flows.
 * Navigation actions are defined to handle transitions between these screens.
 *
 * Utilizes Android Jetpack's Navigation component for managing UI navigation and Hilt for dependency injection.
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Navigation()
        }
    }
}