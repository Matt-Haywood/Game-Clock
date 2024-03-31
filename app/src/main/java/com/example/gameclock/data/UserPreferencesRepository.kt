package com.example.gameclock.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

/**
 * Repository for managing app-wide preferences using DataStore.
 * Currently only manages the fullscreen preference.
 */
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val FULLSCREEN_KEY = booleanPreferencesKey("fullscreen")
        private const val TAG = "UserPreferencesRepo"

    }

    suspend fun setFullscreen(fullscreen: Boolean) {
        dataStore.edit { settings ->
            settings[FULLSCREEN_KEY] = fullscreen
        }
    }

    val fullscreen: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[FULLSCREEN_KEY] ?: false
        }
}

