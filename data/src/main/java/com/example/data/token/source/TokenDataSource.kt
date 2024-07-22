package com.example.data.token.source

interface TokenDataSource {
    interface Local {
        fun getToken(): String?

        fun saveToken(token: String)

        fun clearToken()
    }
}
