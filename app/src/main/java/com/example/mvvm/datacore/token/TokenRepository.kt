package com.example.mvvm.datacore.token

interface TokenRepository {
    fun getToken(): String?

    fun saveToken(token: String)

    fun clearToken()
}
