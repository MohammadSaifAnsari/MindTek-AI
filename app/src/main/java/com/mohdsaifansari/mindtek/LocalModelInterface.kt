package com.mohdsaifansari.mindtek
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow

val MODEL_T5_KEY = booleanPreferencesKey("sum_model_local_change_key")

interface LocalModelInterface {
    fun readModelPreferences(): Flow<Boolean>

    suspend fun saveModelPreferences(isLocalModel: Boolean)
}