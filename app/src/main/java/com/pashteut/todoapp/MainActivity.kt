package com.pashteut.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pashteut.todoapp.ui.DetailScreen
import com.pashteut.todoapp.ui.MainScreen
import com.pashteut.todoapp.ui.theme.ToDoAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ScreenMain
                ) {
                    composable<ScreenMain> {
                        MainScreen(navController = navController)
                    }
                    composable<ScreenDetail> {
                        val args = it.toRoute<ScreenDetail>()
                        DetailScreen(navController = navController, id = args.id)
                    }
                }
            }
        }
    }
}

@Serializable
object ScreenMain

@Serializable
data class ScreenDetail(val id: Long = -1)