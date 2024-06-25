package com.mohdsaifansari.mindtek.AITool.Modal

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.mohdsaifansari.mindtek.Apikey
import kotlinx.coroutines.launch

class AIToolViewModal : ViewModel() {
    val list by lazy {
        mutableStateListOf<String>()
    }
    private val genAi by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = Apikey

        )
    }

    fun sendMessage(message: String) = viewModelScope.launch() {
        var fullResponse = ""
        genAi.generateContentStream(message).collect() { chunk ->
            list.add(chunk.text.toString())
            fullResponse += chunk.text

        }

    }
}