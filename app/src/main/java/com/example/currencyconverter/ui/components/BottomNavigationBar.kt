package com.example.currencyconverter.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.currencyconverter.ui.navigation.BottomNavItem
import com.example.currencyconverter.ui.theme.CustomPrimary
import com.example.currencyconverter.ui.theme.CustomSecondary
import com.example.currencyconverter.ui.theme.CustomTextPrimary

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    // Remembering the current backstack entry to optimize recomposition
    val backStackEntry by navController.currentBackStackEntryAsState()

    // List of Bottom Navigation Tabs
    val tabs = remember {
        listOf(
            BottomNavItem.PairConversion,
            BottomNavItem.ExchangeRates
        )
    }

    // Bottom Navigation Bar with custom colors
    NavigationBar(
        containerColor = Color(0xFFFFF3), // Set background color for the bottom bar
        contentColor = Color(0x9BBEC7), // Set text and icon color in the bottom bar
    ) {
        tabs.forEach { tab ->
            // Check if the current route matches the tab's route
            val selected = tab.route == backStackEntry?.destination?.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    // Navigate to the selected tab's route
                    navController.navigate(tab.route) {
                        // Pop up to the start destination to avoid building up a large stack
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Prevent adding multiple copies of the same destination to the back stack
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected tab
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label // Accessibility label for the icon
                    )
                },
                label = {
                    Text(
                        text = tab.label,
                        color = if (selected) CustomSecondary else CustomTextPrimary // Set label color based on selection
                    )
                },
//                modifier = Modifier.fillMaxWidth()

            )
        }
    }
}
