package com.example.data.token.source

import com.example.data.datastore.TokenStoreApi

class TokenLocalImpl(private val sharedPrefApi: TokenStoreApi) :
    TokenDataSource.Local {
    override fun getToken() = sharedPrefApi.getToken()

    override fun saveToken(token: String) = sharedPrefApi.saveToken(token)

    override fun clearToken() = sharedPrefApi.clearToken()
}
