package com.mohdsaifansari.mindtek.Setting

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow

val THEME_KEY = booleanPreferencesKey("theme_change_key")

interface ThemeInterface {
    fun readThemePreferences(): Flow<Boolean>

    suspend fun saveThemePreferences(isDarkTheme: Boolean)
}