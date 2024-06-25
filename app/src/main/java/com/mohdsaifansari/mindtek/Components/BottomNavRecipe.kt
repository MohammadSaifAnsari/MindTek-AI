package com.mohdsaifansari.mindtek.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch


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
        val coroutineScope = rememberCoroutineScope()

        items.forEach { item ->
            NavigationBarItem(
                selected = currentState == item.route,
                onClick = {
                    coroutineScope.launch {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(item.route)
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Transparent)
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemColors(
                    selectedIconColor = Color(70, 76, 91, 255),
                    unselectedIconColor = Color.Gray, selectedTextColor = Color(70, 76, 91, 255),
                    unselectedTextColor = Color.Gray, disabledTextColor = Color.LightGray,
                    disabledIconColor = Color.LightGray, selectedIndicatorColor = Color.Transparent
                )

            )
        }
    }

}


