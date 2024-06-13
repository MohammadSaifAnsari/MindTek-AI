package com.mohdsaifansari.mindtek.ui.theme.Components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun MainBottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.AItools,
        BottomNavItem.ChatBot,
        BottomNavItem.History,
        BottomNavItem.Profile
    )
    BottomAppBar(
        containerColor = Color.White,
        contentColor = Color.Black,
        modifier = Modifier.height(95.dp)
    ) {
        val navStack by navController.currentBackStackEntryAsState()
        val currentState = navStack?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                label = {
                    Text(item.label)
                },
                selected = currentState == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(item.route)
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemColors(
                    selectedIconColor = Color.Green,
                    unselectedIconColor = Color.DarkGray, selectedTextColor = Color.Magenta,
                    unselectedTextColor = Color.DarkGray, disabledTextColor = Color.LightGray,
                    disabledIconColor = Color.LightGray, selectedIndicatorColor = Color.Transparent
                )

            )
        }
    }

}


