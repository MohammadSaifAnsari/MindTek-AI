package com.mohdsaifansari.mindtek.ui.theme.ChatBotAi

sealed interface ChatBotUiState {
    data object Ideal: ChatBotUiState
    data object Loading: ChatBotUiState
    data class Success(val ChatData: String): ChatBotUiState
    data class Error(val ChatError:String): ChatBotUiState
}