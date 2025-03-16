package com.example.currencyconverter.ui.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object PairConversion : BottomNavItem(
        "pairConversionScreen",
        Icons.Filled.Home,
        "Home"
    )
    object ExchangeRates : BottomNavItem(
        "exchangeRatesScreen",
        Icons.Filled.Search,
        "Exchange Rates"
    )
}