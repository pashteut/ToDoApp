package com.pashteut.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pashteut.todoapp.features.todo_details.ui.DetailScreen
import com.pashteut.todoapp.features.todo_list.ui.MainScreen
import com.pashteut.todoapp.features.todo_auth.ui.AuthScreen
import com.pashteut.todoapp.features.todo_details.ui_logic.DetailScreenViewModel
import com.pashteut.todoapp.ui_kit.ToDoAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

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
            ToDoAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ScreenMain
                ) {
                    composable<ScreenMain> {
                        MainScreen(
                            addItemNavigation = { navController.navigate(ScreenDetail()) },
                            editItemNavigation = { id -> navController.navigate(ScreenDetail(id)) },
                            authNavigation = { navController.navigate(ScreenAuth) },
                            viewModel = hiltViewModel()
                        )
                    }
                    composable<ScreenDetail> {
                        val args = it.toRoute<ScreenDetail>()
                        val viewModel : DetailScreenViewModel = hiltViewModel()
                        viewModel.setPickedItem(args.id)
                        DetailScreen(
                            mainScreenNavigation = { navController.navigateUp() },
                            viewModel = viewModel
                        )
                    }
                    composable<ScreenAuth> {
                        AuthScreen(
                            mainScreenNavigation = { navController.navigate(ScreenMain) },
                            viewModel = hiltViewModel()
                        )
                    }
                }
            }
        }
    }
}

@Serializable
object ScreenMain

@Serializable
data class ScreenDetail(val id: String? = null)

@Serializable
object ScreenAuth