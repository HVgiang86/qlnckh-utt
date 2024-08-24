package com.example.mvvm.datacore.token

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = com.example.mvvm.constants.TokenStoreKey.PREFS_NAME)
private val TOKEN_KEY = stringPreferencesKey(com.example.mvvm.constants.TokenStoreKey.KEY_TOKEN)

class TokenStoreApiImpl(private val context: Context) : TokenStoreApi {
    override fun getToken(): String? {
        var token: String? = null
        runBlocking {
            val flow = context.dataStore.data.map {
                it[TOKEN_KEY]
            }

            token = flow.last()
        }

        return token
    }

    override fun saveToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit {
                it[TOKEN_KEY] = token
            }
        }
    }

    override fun clearToken() {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit {
                it.remove(TOKEN_KEY)
            }
        }
    }
}
