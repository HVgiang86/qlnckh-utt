package com.example.data.token

interface TokenRepository {
    fun getToken(): String?

    fun saveToken(token: String)

    fun clearToken()
}
