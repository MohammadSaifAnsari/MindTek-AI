package com.mohdsaifansari.mindtek

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "change_Model")

class LocalModelPreference(private val datastore: DataStore<Preferences>) : LocalModelInterface {


    override fun readModelPreferences(): Flow<Boolean> {
        return datastore.data.catch { emit(emptyPreferences()) }.map {
            it[com.mohdsaifansari.mindtek.MODEL_T5_KEY] ?: true
        }
    }

    override suspend fun saveModelPreferences(isLocalModel: Boolean) {
        datastore.edit {
            it[com.mohdsaifansari.mindtek.MODEL_T5_KEY] = isLocalModel
        }
    }

}