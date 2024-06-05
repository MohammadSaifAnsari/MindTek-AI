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

data class loadHistory(
    val idNo: Int,
    val prompt: String,
    val result: String,
    val day: String,
    val month: String,
    val year: String,
)