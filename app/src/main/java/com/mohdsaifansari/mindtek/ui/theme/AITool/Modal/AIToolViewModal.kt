package com.mohdsaifansari.mindtek.ui.theme.AITool.Modal

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.mohdsaifansari.mindtek.Apikey
import com.mohdsaifansari.mindtek.ui.theme.ChatBotAi.ChatData
import kotlinx.coroutines.launch

class AIToolViewModal: ViewModel() {
    val list by lazy{
        mutableStateListOf<String>()
    }
    private val genAi by lazy{
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = Apikey

        )
    }

     fun sendMessage(message:String) = viewModelScope.launch() {
        var response:String? = genAi.startChat().sendMessage(prompt = message).text
         list.add(response.toString())

    }
}