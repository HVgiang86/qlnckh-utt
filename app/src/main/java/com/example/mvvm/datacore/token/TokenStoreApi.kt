package com.example.mvvm.datacore.token

interface TokenStoreApi {
    fun getToken(): String?

    fun saveToken(token: String)

    fun clearToken()

    fun getCookie(): String?

    fun saveCookie(cookie: String)

    fun clearCookie()
}
