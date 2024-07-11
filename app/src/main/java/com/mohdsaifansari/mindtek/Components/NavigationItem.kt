package com.mohdsaifansari.mindtek.Components

sealed class NavigationItem(val route: String) {
    object SignIn : NavigationItem(route = "SignIn")
    object SignUp : NavigationItem(route = "Sign_Up")
    object MainScreen : NavigationItem(route = "Main_Screen")
    object AuthScreen : NavigationItem(route = "Auth_Screen_nav")
    object HomeScreen : NavigationItem(route = "Home_Screen_nav")
    object ProfileEditNav : NavigationItem(route = "Profile_Edit_nav")
    object ProfileEditScreen : NavigationItem(route = "Profile_Edit")
    object ProfileItemNav : NavigationItem(route = "Profile_Item_nav/{item_name}")
    object ProfileItemScreen : NavigationItem(route = "Profile_Item_Screen")
    object ResultNav : NavigationItem(route = "Result_nav/{result}")
    object ResultScreen : NavigationItem(route = "Result_Screen")
    object SettingNav : NavigationItem(route = "Setting_nav")
    object SettingScreen : NavigationItem(route = "Setting_Screen")
    object SummarizerNav : NavigationItem(route = "Summarizer_nav/{title}/{subtitle}")
    object SummarizerScreen : NavigationItem(route = "Summarizer_Screen")
    object ToolNav : NavigationItem(route = "Tool_nav/{title}/{subtitle}")
    object ToolScreen : NavigationItem(route = "Tool_Screen")


}