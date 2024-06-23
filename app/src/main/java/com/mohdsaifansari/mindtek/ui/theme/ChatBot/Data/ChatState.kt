package com.mohdsaifansari.mindtek.ui.theme.ChatBot.Data

import android.app.VoiceInteractor.Prompt
import android.graphics.Bitmap

data class ChatState(
    val chatList: MutableList<ChatWithUri> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
)
