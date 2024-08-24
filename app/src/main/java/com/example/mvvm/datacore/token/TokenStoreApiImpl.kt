package com.example.mvvm.datacore.token

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mvvm.constants.TokenStoreKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = TokenStoreKey.PREFS_NAME)
private val TOKEN_KEY = stringPreferencesKey(TokenStoreKey.KEY_TOKEN)

class TokenStoreApiImpl @Inject constructor(private val context: Context) : TokenStoreApi {

    private val sharedPreferences = context.getSharedPreferences(TokenStoreKey.PREFS_NAME, Context.MODE_PRIVATE)
    override fun getToken(): String? {
        val token = sharedPreferences.getString(TokenStoreKey.KEY_TOKEN, null)
        return token
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString(TokenStoreKey.KEY_TOKEN, token).apply()
    }

    override fun clearToken() {
        sharedPreferences.edit().remove(TokenStoreKey.KEY_TOKEN).apply()
    }
}
