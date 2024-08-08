package com.example.taskapp.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject


class DataStoreManagerImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    DataStoreManager {

    override suspend fun saveToken(value: String) {
        dataStore.edit { it[stringPreferencesKey("token")] = value }
    }

    override suspend fun getToken(): String? {
        return dataStore.data.firstOrNull()?.get(stringPreferencesKey("token"))
    }

    override suspend fun saveUserId(value: Int) {
        dataStore.edit { it[intPreferencesKey("userId")] = value }
    }

    override suspend fun getUserId(): Int? {
        return dataStore.data.firstOrNull()?.get(intPreferencesKey("userId"))
    }

    override suspend fun saveUserEmail(value: String) {
        dataStore.edit { it[stringPreferencesKey("email")] = value }
    }

    override suspend fun getUserEmail(): String? {
        return dataStore.data.firstOrNull()?.get(stringPreferencesKey("email"))
    }

}
