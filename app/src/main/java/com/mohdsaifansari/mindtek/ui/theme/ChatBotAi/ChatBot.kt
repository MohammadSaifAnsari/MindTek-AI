package com.mohdsaifansari.mindtek.ui.theme.ChatBotAi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChatBot(
    viewmodel: ChatBotViewmodel = viewModel()
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        ChatHeader()

        Box (modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center){
            if (viewmodel.list.isEmpty()){
                Text(text = "No Chat Available")
            }else{
                ChatList(list = viewmodel.list)
            }

        }
        ChatFooter {
            if(it.isNotEmpty()){
                viewmodel.sendMessage(it)
            }
        }
    }
}