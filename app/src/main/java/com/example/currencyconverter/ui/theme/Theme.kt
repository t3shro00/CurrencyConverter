package com.example.currencyconverter.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val lightColorPalette = lightColorScheme(
    primary = CustomPrimary,
    secondary = CustomSecondary,
    surface = CustomSurface,
    background = CustomBackground,
    onPrimary = Color.White, // text color for primary color
    onSecondary = Color.White, // text color for secondary color
    onSurface = Color.Black, // text color for surface color
    onBackground = Color.Black // text color for background color
)

@Composable
fun CurrencyConverterTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorPalette,
        content = content
    )
}
