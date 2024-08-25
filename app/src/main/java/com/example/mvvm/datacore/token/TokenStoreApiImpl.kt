package com.example.mvvm.datacore.token

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mvvm.constants.TokenStoreKey
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = TokenStoreKey.PREFS_NAME)
private val TOKEN_KEY = stringPreferencesKey(TokenStoreKey.KEY_TOKEN)

class TokenStoreApiImpl
@Inject
constructor(private val context: Context) : TokenStoreApi {

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

    override fun getCookie(): String? {
        val cookie = sharedPreferences.getString(TokenStoreKey.KEY_COOKIE, null)
        return cookie
    }

    override fun saveCookie(cookie: String) {
        sharedPreferences.edit().putString(TokenStoreKey.KEY_COOKIE, cookie).apply()
    }

    override fun clearCookie() {
        sharedPreferences.edit().remove(TokenStoreKey.KEY_COOKIE).apply()
    }

    override fun getLoginState(): Boolean {
        return sharedPreferences.getBoolean(TokenStoreKey.KEY_LOGIN_STATE, false)
    }

    override fun setLoginState(state: Boolean) {
        sharedPreferences.edit().putBoolean(TokenStoreKey.KEY_LOGIN_STATE, state).apply()
    }
}
