package com.example.data.token

import com.example.data.token.source.TokenDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryImpl
    @Inject
    constructor(private val local: TokenDataSource.Local) : TokenRepository {
        override fun getToken() = local.getToken()

        override fun saveToken(token: String) = local.saveToken(token)

        override fun clearToken() = local.clearToken()
    }
