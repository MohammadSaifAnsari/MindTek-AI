package com.mohdsaifansari.mindtek.ui.theme.ChatBot.Data

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.mohdsaifansari.mindtek.Apikey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChatData {
    val api_key = Apikey

    suspend fun getResponse(prompt: String): ChatWithUri {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash", apiKey = api_key
        )
        try {
            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(prompt)
            }
            return ChatWithUri(
                timestamp = System.currentTimeMillis(),
                message = response.text ?: "error",
                imageAddress = "",
                isUser = false
            )
        } catch (e: Exception) {
            return ChatWithUri(
                timestamp = System.currentTimeMillis(),
                message = e.message ?: "error",
                imageAddress = "",
                isUser = false
            )
        }
    }

    suspend fun getResponseImage(prompt: String, bitmap: Bitmap): ChatWithUri {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash", apiKey = api_key
        )
        try {
            val inputContent = content {
                image(bitmap)
                text(prompt)
            }
            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(inputContent)
            }
            return ChatWithUri(
                timestamp = System.currentTimeMillis(),
                message = response.text ?: "error",
                imageAddress = "",
                isUser = false
            )
        } catch (e: Exception) {
            return ChatWithUri(
                timestamp = System.currentTimeMillis(),
                message = e.message ?: "error",
                imageAddress = "",
                isUser = false
            )
        }
    }
}