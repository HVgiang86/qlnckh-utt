package com.example.data.datastore

interface TokenStoreApi {
    fun getToken(): String?

    fun saveToken(token: String)

    fun clearToken()
}
