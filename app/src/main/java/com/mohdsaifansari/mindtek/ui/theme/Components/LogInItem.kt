package com.mohdsaifansari.mindtek.ui.theme.Components

sealed class LogInItem(val route: String) {
    object SignIn : LogInItem(route = "SignIn")
    object SignUp : LogInItem(route = "Sign_Up")
    object MainScreen : LogInItem(route = "Main_Screen")
    object AuthScreen : LogInItem(route = "Auth_Screen")
    object HomeScreen : LogInItem(route = "Home_Screen")

}