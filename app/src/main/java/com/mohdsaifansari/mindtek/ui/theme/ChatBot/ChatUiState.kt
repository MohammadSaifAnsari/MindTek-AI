package com.mohdsaifansari.mindtek.ui.theme.ChatBot

import android.graphics.Bitmap

sealed class ChatUiState {
    data class UpdatePrompt(val newPrompt:String): ChatUiState()
    data class SendPrompt(val prompt:String,
        val bitmap: Bitmap?):ChatUiState()
}