package com.example.datastoretest

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property za DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
    
    // Ključi za shranjevanje vrednosti
    companion object {
        val TOGGLE_1_KEY = booleanPreferencesKey("toggle_1")
        val TOGGLE_2_KEY = booleanPreferencesKey("toggle_2")
        val TOGGLE_3_KEY = booleanPreferencesKey("toggle_3")
    }
    
    // Flow za branje vrednosti toggle 1
    val toggle1Flow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[TOGGLE_1_KEY] ?: false
        }
    
    // Flow za branje vrednosti toggle 2
    val toggle2Flow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[TOGGLE_2_KEY] ?: false
        }
    
    // Flow za branje vrednosti toggle 3
    val toggle3Flow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[TOGGLE_3_KEY] ?: false
        }
    
    // Shranjevanje vrednosti toggle 1
    suspend fun saveToggle1(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TOGGLE_1_KEY] = value
        }
    }
    
    // Shranjevanje vrednosti toggle 2
    suspend fun saveToggle2(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TOGGLE_2_KEY] = value
        }
    }
    
    // Shranjevanje vrednosti toggle 3
    suspend fun saveToggle3(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TOGGLE_3_KEY] = value
        }
    }
}

