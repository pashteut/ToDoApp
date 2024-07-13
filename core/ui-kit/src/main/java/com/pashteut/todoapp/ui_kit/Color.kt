package com.pashteut.todoapp.ui_kit

import androidx.compose.ui.graphics.Color

val lightColors = Colors(
    blue = Color(0xFF007AFF),
    red = Color(0xFFff3b30),
    green = Color(0xFF34C759),
    gray = Color(0xFF8E8E93),
    grayLight = Color(0xFFD1D1D6),
    label = Color(0xFF000000),
    labelSecondary = Color(0x99000000),
    labelTertiary = Color(0x4d000000),
    labelDisabled = Color(0x26000000),
    backPrimary = Color(0xFFF7F6F2),
    backSecondary = Color(0xFFFFFFFF),
    backElevated = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xffffffff),
    supportSeparator = Color(0x33000000),
    supportOverlay = Color(0x0f000000),
)

val darkColors = Colors(
    blue = Color(0xFF0A84FF),
    red = Color(0xFFff453a),
    green = Color(0xFF32D74B),
    gray = Color(0xFF8E8E93),
    grayLight = Color(0xFF48484A),
    label = Color(0xFFFFFFFF),
    labelSecondary = Color(0xFFFFFFFF).copy(alpha = 0.6f),
    labelTertiary = Color(0xFFFFFFFF).copy(alpha = 0.4f),
    labelDisabled = Color(0xFFFFFFFF).copy(alpha = 0.4f),
    backPrimary = Color(0xFF1C1B1F),
    backSecondary = Color(0xFF2C2C2E),
    backElevated = Color(0xFF2C2C2E),
    surfaceVariant = Color(0xff252528),
    supportSeparator = Color(0x33ffffff),
    supportOverlay = Color(0x52000000),
)

/**
 * Defines the color palette for the ToDo application's UI theme.
 */

data class Colors(
    val blue: Color,
    val red: Color,
    val green: Color,
    val gray: Color,
    val grayLight: Color,
    val label: Color,
    val labelSecondary: Color,
    val labelTertiary: Color,
    val labelDisabled: Color,
    val backPrimary: Color,
    val backSecondary: Color,
    val backElevated: Color,
    val surfaceVariant : Color,
    val supportSeparator: Color,
    val supportOverlay : Color,
)