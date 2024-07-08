package com.mohdsaifansari.mindtek.Setting.ThemeChange

import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow

val THEME_KEY = booleanPreferencesKey("theme_change_key")

interface ThemeInterface {
    fun readThemePreferences(): Flow<Boolean>

    suspend fun saveThemePreferences(isDarkTheme: Boolean)
}