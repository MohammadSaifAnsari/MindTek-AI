package com.mohdsaifansari.mindtek.Setting.ThemeChange

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "change_Theme")

class ThemePreference(private val datastore: DataStore<Preferences>) : ThemeInterface {


    override fun readThemePreferences(): Flow<Boolean> {
        return datastore.data.catch { emit(emptyPreferences()) }.map {
            it[THEME_KEY] ?: false
        }
    }

    override suspend fun saveThemePreferences(isDarkTheme: Boolean) {
        datastore.edit {
            it[THEME_KEY] = isDarkTheme
        }
    }

}