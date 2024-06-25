package com.mohdsaifansari.mindtek.ChatBot.Data

import android.graphics.Bitmap

data class ChatState(
    val chatList: MutableList<ChatWithUri> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
)
