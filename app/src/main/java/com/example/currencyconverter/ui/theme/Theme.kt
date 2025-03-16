package com.example.currencyconverter.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define the color schemes
val LightColorScheme = lightColorScheme(
    primary = CustomPrimary,
    secondary = CustomSecondary,
    background = CustomBackground,
    surface = CustomSurface,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = CustomTextPrimary,
    onSurface = CustomTextPrimary
)

val DarkColorScheme = darkColorScheme(
    primary = CustomPrimary,
    secondary = CustomSecondary,
    background = Color.Black,
    surface = Color.DarkGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun CurrencyConverterTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
