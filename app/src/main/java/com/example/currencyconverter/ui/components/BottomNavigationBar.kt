package com.example.currencyconverter.ui.components



import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val tabs = listOf(
        BottomNavItem.PairConversion,
        BottomNavItem.ExchangeRates
    )

    NavigationBar {
        tabs.forEach { tab ->
            val selected = tab.route == backStackEntry?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(tab.route) {
                        // Pop up to the start destination to avoid building up a large stack
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when reelecting a previously selected item
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label
                    )
                },
                label = { Text(tab.label) }
            )
        }
    }
}


//@Composable
//fun BottomNavigationBar(navController: NavController) {
//    val items = listOf(
//        BottomNavItem.PairConversion,
//        BottomNavItem.ExchangeRates
//    )
//
//    BottomNavigation(
//        backgroundColor = MaterialTheme.colorScheme.surface,
//        contentColor = MaterialTheme.colorScheme.onSurface,
//        elevation = 8.dp
//    ) {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentDestination = navBackStackEntry?.destination
//
//        items.forEach { item ->
//            BottomNavigationItem(
//                icon = { Icon(painter = painterResource(id = item.icon), contentDescription = item.label) },
//                label = { Text(item.label) },
//                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
//                onClick = {
//                    navController.navigate(item.route) {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                },
//                selectedContentColor = MaterialTheme.colorScheme.primary,
//                unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//            )
//        }
//    }
//}