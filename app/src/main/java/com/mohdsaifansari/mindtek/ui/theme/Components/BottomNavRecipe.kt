package com.mohdsaifansari.mindtek.ui.theme.Components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mohdsaifansari.mindtek.ui.theme.AImageScreen
import com.mohdsaifansari.mindtek.ui.theme.AiToolScreen
import com.mohdsaifansari.mindtek.ui.theme.ChatBotAi.ChatBot
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mohdsaifansari.mindtek.MainActivity
import com.mohdsaifansari.mindtek.ui.theme.ChatBotAi.ChatHeader





@Composable
fun MainBottomNavigation(navController: NavController){
    val items = listOf(
        BottomNavItem.AItools,
        BottomNavItem.ChatBot,
        BottomNavItem.AImages,
    )
    BottomAppBar(containerColor = Color.White, contentColor = Color.White, modifier = Modifier.height(95.dp)){
        val navStack by navController.currentBackStackEntryAsState()
        val currentState = navStack?.destination?.route
        
        items.forEach{ item ->
            NavigationBarItem(
                label = {
                        Text(item.label)
                }, 
                selected = currentState == item.route, 
                onClick = { 
                    navController.navigate(item.route){
                        navController.graph.startDestinationRoute?.let{
                            popUpTo(item.route)
                            launchSingleTop = true
                            restoreState = true
                        }
                    }      
                },
                icon = {
                    Icon(imageVector = item.icon, 
                        contentDescription = null,
                        modifier = Modifier.size(24.dp))
                }, 
                alwaysShowLabel = true ,
                colors = NavigationBarItemColors(selectedIconColor = Color.Red,
                    unselectedIconColor = Color.Black, selectedTextColor = Color.Red,
                    unselectedTextColor = Color.Black, disabledTextColor = Color.Black,
                    disabledIconColor = Color.Black, selectedIndicatorColor = Color.White)
                
            )
        }
    }

}


