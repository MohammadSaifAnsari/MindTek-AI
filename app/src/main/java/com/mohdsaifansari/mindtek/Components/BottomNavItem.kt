package com.mohdsaifansari.mindtek.Components

import com.mohdsaifansari.mindtek.R

sealed class BottomNavItem(val icon: Int, val label: String, val route: String) {
    object AItools :
        BottomNavItem(icon = R.drawable.tool, label = "AI Tools", route = "aitools")

    object ChatBot :
        BottomNavItem(icon = R.drawable.chatbot, label = "ChatBot", route = "chatbot")

    object Profile :
        BottomNavItem(icon = R.drawable.account, label = "Account", route = "user_profile")

    object History :
        BottomNavItem(icon = R.drawable.history, label = "History", route = "toolhistory")
}