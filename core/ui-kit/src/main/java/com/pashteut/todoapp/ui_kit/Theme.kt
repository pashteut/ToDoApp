package com.pashteut.todoapp.ui_kit

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
    get() = if (isSystemInDarkTheme()) darkColors else lightColors

@Composable
fun ToDoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}