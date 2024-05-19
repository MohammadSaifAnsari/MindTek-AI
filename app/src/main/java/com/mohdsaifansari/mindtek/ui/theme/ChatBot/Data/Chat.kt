package com.mohdsaifansari.mindtek.ui.theme.ChatBot.Data

import android.graphics.Bitmap

data class Chat(
    val prompt:String,
    val bitmap: Bitmap?,
    val isFromUser: Boolean)

