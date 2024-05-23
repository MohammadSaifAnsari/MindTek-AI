package com.mohdsaifansari.mindtek.ui.theme.ChatBotAi

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.mohdsaifansari.mindtek.Apikey
import kotlinx.coroutines.launch

class ChatBotViewmodel : ViewModel(){

    val list by lazy{
        mutableStateListOf<ChatData>()
    }
    private val genAi by lazy{
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = Apikey

        )
    }
    fun sendMessage(message:String) = viewModelScope.launch {
//        var response:String? = genAi.startChat().sendMessage(prompt = message).text
        val chat : Chat = genAi.startChat()
        list.add(ChatData(message, ChatRoleEnum.USER.role))
        chat.sendMessage(content(ChatRoleEnum.USER.role ) { text(message)}).text?.let {
            list.add(ChatData(it, ChatRoleEnum.MODEL.role))
        }
    }
}