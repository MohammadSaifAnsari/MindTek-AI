package com.mohdsaifansari.mindtek.ui.theme.ChatBot

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohdsaifansari.mindtek.ui.theme.ChatBot.Data.Chat
import com.mohdsaifansari.mindtek.ui.theme.ChatBot.Data.ChatData
import com.mohdsaifansari.mindtek.ui.theme.ChatBot.Data.ChatState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel:ViewModel() {
    private val chat_state = MutableStateFlow(ChatState())
    val chatStat = chat_state.asStateFlow()

    fun onEvent(event:ChatUiState){
        when(event){
            is ChatUiState.SendPrompt ->{
                if (event.prompt.isNotEmpty()){
                    addPrompt(event.prompt,event.bitmap)
                }
                if (event.bitmap!= null){
                    getResponseImage(event.prompt,event.bitmap)
                }else{
                    getResponse(event.prompt)
                }
            }
            is ChatUiState.UpdatePrompt ->{
                chat_state.update {
                    it.copy(prompt = event.newPrompt)
                }
            }
        }
    }

    private fun addPrompt(prompt:String,bitmap: Bitmap?){
        chat_state.update {
            it.copy(
                chatList = it.chatList.toMutableList().apply {
                    add(0, Chat(prompt,bitmap,true))
                },
                prompt = "",
                bitmap = null
            )
        }
    }

    private fun getResponse(prompt: String){
        viewModelScope.launch {
            val chat = ChatData.getResponse(prompt)
            chat_state.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat)
                    },
                )
            }
        }
    }
    private fun getResponseImage(prompt: String , bitmap: Bitmap){
        viewModelScope.launch {
            val chat = ChatData.getResponseImage(prompt,bitmap)
            chat_state.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat)
                    },
                )
            }
        }
    }
}