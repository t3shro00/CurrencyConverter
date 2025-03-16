// CurrencyConverterApp.kt
package com.example.currencyconverter

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter.ui.components.BottomNavItem
import com.example.currencyconverter.ui.components.BottomNavigationBar
import com.example.currencyconverter.ui.screens.ExchangeRatesScreen
import com.example.currencyconverter.ui.screens.PairConversionScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val title = when (currentRoute) {
        BottomNavItem.PairConversion.route -> BottomNavItem.PairConversion.label
        BottomNavItem.ExchangeRates.route -> BottomNavItem.ExchangeRates.label
        else -> "Currency Converter"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.shadow(elevation = 8.dp)
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.PairConversion.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(BottomNavItem.PairConversion.route) {
                    PairConversionScreen(
                        viewModel = viewModel(),
                        navController = navController
                    )
                }
                composable(BottomNavItem.ExchangeRates.route) {
                    ExchangeRatesScreen(
                        viewModel = viewModel(),
                        navController = navController
                    )
                }
            }
        }
    )
}