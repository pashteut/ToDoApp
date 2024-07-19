package com.pashteut.todoapp.ui_kit

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.pashteut.todoapp.ui_kit.theme_state.ThemeState

private val DarkColorScheme = darkColorScheme(
    primary = darkColors.blue,
    primaryContainer = darkColors.blue,
    onPrimaryContainer = Color.White,
    background = darkColors.backPrimary,
    onSurfaceVariant = darkColors.blue,
    surface = darkColors.backPrimary,
    surfaceContainer = darkColors.surfaceVariant,
    error = darkColors.red,
    outline = darkColors.gray,
    outlineVariant = darkColors.supportSeparator,
)

private val LightColorScheme = lightColorScheme(
    primary = lightColors.blue,
    primaryContainer = lightColors.blue,
    onPrimaryContainer = lightColors.label,
    background = lightColors.backPrimary,
    onSurfaceVariant = lightColors.blue,
    surface = lightColors.backPrimary,
    surfaceContainer = lightColors.surfaceVariant,
    error = lightColors.red,
    outline = lightColors.gray,
    outlineVariant = lightColors.supportSeparator,
)

val ColorScheme.additionalColors: Colors
    @Composable
    get() = if (_isDarkTheme) darkColors else lightColors

private var _isDarkTheme: Boolean = false
val isDarkTheme get() = _isDarkTheme

@Composable
fun ToDoAppTheme(
    themeState: ThemeState = ThemeState.System,
    content: @Composable () -> Unit
) {
    _isDarkTheme = when (themeState) {
        ThemeState.System -> isSystemInDarkTheme()
        ThemeState.Dark -> true
        ThemeState.Light -> false
    }
    val colorScheme = if (_isDarkTheme) DarkColorScheme else LightColorScheme

    SetSystemBarsColor(
        color = colorScheme.background,
        darkIcons = !_isDarkTheme
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun SetSystemBarsColor(color: Color, darkIcons: Boolean) {
    val activity = LocalContext.current as? Activity
    activity?.window?.let { window ->
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = darkIcons
        insetsController.isAppearanceLightNavigationBars = darkIcons
        window.statusBarColor = color.toArgb()
    }
}