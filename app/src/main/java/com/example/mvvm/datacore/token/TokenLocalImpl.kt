package com.example.mvvm.datacore.token

import javax.inject.Inject

class TokenLocalImpl @Inject constructor(private val sharedPrefApi: TokenStoreApi) : TokenDataSource.Local {
    override fun getToken() = sharedPrefApi.getToken()

    override fun saveToken(token: String) = sharedPrefApi.saveToken(token)

    override fun clearToken() = sharedPrefApi.clearToken()
}
