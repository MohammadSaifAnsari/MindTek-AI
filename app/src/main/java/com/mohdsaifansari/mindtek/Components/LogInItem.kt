package com.mohdsaifansari.mindtek.Components

sealed class LogInItem(val route: String) {
    object SignIn : LogInItem(route = "SignIn")
    object SignUp : LogInItem(route = "Sign_Up")
    object MainScreen : LogInItem(route = "Main_Screen")
    object AuthScreen : LogInItem(route = "Auth_Screen_nav")
    object HomeScreen : LogInItem(route = "Home_Screen_nav")
    object ProfileEditNav : LogInItem(route = "Profile_Edit_nav")
    object ProfileEditScreen : LogInItem(route = "Profile_Edit")
    object ProfileItemNav : LogInItem(route = "Profile_Item_nav/{item_name}")
    object ProfileItemScreen : LogInItem(route = "Profile_Item_Screen")
    object ResultNav : LogInItem(route = "Result_nav/{result}")
    object ResultScreen : LogInItem(route = "Result_Screen")
    object SettingNav : LogInItem(route = "Setting_nav")
    object SettingScreen : LogInItem(route = "Setting_Screen")


}