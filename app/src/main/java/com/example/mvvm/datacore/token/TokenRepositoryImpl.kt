package com.example.mvvm.datacore.token

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
