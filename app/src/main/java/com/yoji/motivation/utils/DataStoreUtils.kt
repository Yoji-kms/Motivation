package com.yoji.motivation.utils

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.yoji.motivation.application.App
import com.yoji.motivation.fragments.LoginFragment.Companion.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object DataStoreUtils {
    fun getString(key: Preferences.Key<String>) = runBlocking {
            App.appContext().dataStore.data.map { preferences ->
                preferences[key]
            }.first()
        } ?: ""

    fun getLong(key: Preferences.Key<Long>) = runBlocking {
        App.appContext().dataStore.data.map { preferences ->
            preferences[key]
        }.first()
    } ?: 0L

    suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        App.appContext().dataStore.edit { settings ->
            settings[key] = value
        }
    }
}