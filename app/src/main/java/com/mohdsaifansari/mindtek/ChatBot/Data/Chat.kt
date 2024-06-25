package com.mohdsaifansari.mindtek.ChatBot.Data


data class ChatWithUri(
    val timestamp: Long,
    val message: String,
    val imageAddress: String,
    val isUser: Boolean

)
