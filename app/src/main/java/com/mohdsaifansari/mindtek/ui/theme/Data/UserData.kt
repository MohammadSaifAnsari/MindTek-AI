package com.mohdsaifansari.mindtek.ui.theme.Data

data class UserData(
    val firstName: String,
    val lastName: String,
    val email: String
)

data class Date(
    val dayofMonth: String,
    val month: String,
    val year: String,
)

data class LoadHistory(
    val idNo: Int?,
    val prompt: String?,
    val result: String?,
    val day: String?,
    val month: String?,
    val year: String?,
)

data class DrawerItem(
    val icon:Int,
    val title: String

)