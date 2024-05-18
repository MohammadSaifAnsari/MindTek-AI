package com.mohdsaifansari.mindtek.ui.theme.Components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val icon: ImageVector , val label:String,val route:String) {
    object AItools :BottomNavItem(icon = Icons.Default.Home, label = "AI Tools", route = "aitools")
    object ChatBot :BottomNavItem(icon = Icons.Default.AccountBox, label = "ChatBot", route = "chatbot")
    object AImages :BottomNavItem(icon = Icons.Default.Face, label = "AI Image", route = "aimage")
}