package com.mohdsaifansari.mindtek.ui.theme.Data

import com.mohdsaifansari.mindtek.R

sealed class ProfileItem(val name: String, val painter: Int) {
    object Personalinfo : ProfileItem(name = "Personal info", painter = R.drawable.personalinfo)
    object HelpCenter : ProfileItem(name = "Help Center", painter = R.drawable.helpcenter)
    object PrivacyPolicy : ProfileItem(name = "Privacy Policy", painter = R.drawable.privacypolicy)
    object AboutMindtek : ProfileItem(name = "About Mindtek", painter = R.drawable.about)
    object Logout : ProfileItem(name = "Logout", painter = R.drawable.logout)
}

sealed class ProfilePhotoKey(val name:String){
    object ProfileScreenPhotoKey : ProfilePhotoKey(name = "Profile Screen")
    object NavDrawerPhotoKey : ProfilePhotoKey(name = "Nav Drawer Screen")
}