package com.pashteut.todoapp

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pashteut.todoapp.features.todo_about.AboutScreen
import com.pashteut.todoapp.features.todo_auth.ui.AuthScreen
import com.pashteut.todoapp.features.todo_details.ui.DetailScreen
import com.pashteut.todoapp.features.todo_details.ui_logic.DetailScreenViewModel
import com.pashteut.todoapp.features.todo_list.ui.MainScreen
import com.pashteut.todoapp.features.todo_settings.ui.SettingsScreen
import com.pashteut.todoapp.ui_kit.ToDoAppTheme

@Composable
fun Navigation() {
    val viewModel: AppViewModel = hiltViewModel()
    val themeState by viewModel.themeState.collectAsStateWithLifecycle()

    ToDoAppTheme(themeState = themeState) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = ScreenMain
        ) {
            composable<ScreenMain> {
                MainScreen(
                    addItemNavigation = { navController.navigate(ScreenDetail()) },
                    editItemNavigation = { id -> navController.navigate(ScreenDetail(id)) },
                    settingsNavigation = { navController.navigate(ScreenSettings) },
                    viewModel = hiltViewModel(),
                )
            }
            composable<ScreenDetail>(
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(350)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(350)
                    )
                }
            ) {
                val args = it.toRoute<ScreenDetail>()
                val detailScreenViewModel: DetailScreenViewModel = hiltViewModel()
                detailScreenViewModel.setPickedItem(args.id)
                DetailScreen(
                    mainScreenNavigation = { navController.navigateUp() },
                    viewModel = detailScreenViewModel
                )
            }
            composable<ScreenAuth> {
                AuthScreen(
                    backNavigation = { navController.navigateUp() },
                    viewModel = hiltViewModel()
                )
            }
            composable<ScreenSettings>(
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(350)
                    )
                },
            ) {
                SettingsScreen(
                    backNavigation = { navController.navigateUp() },
                    aboutScreenNavigation = { navController.navigate(ScreenAbout) },
                    authNavigation = { navController.navigate(ScreenAuth) },
                    viewModel = hiltViewModel(),
                )
            }
            composable<ScreenAbout>(
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(350)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(350)
                    )
                }
            ) {
                AboutScreen(
                    backNavigation = { navController.navigateUp() }
                )
            }
        }
    }
}