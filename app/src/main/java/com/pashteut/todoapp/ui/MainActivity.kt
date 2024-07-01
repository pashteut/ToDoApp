package com.pashteut.todoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pashteut.todoapp.presentator.DetailScreenViewModel
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
                        MainScreen(
                            addItemNavigation = { navController.navigate(ScreenDetail()) },
                            editItemNavigation = { id -> navController.navigate(ScreenDetail(id)) },
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
                }
            }
        }
    }
}

@Serializable
object ScreenMain

@Serializable
data class ScreenDetail(val id: Long = -1)