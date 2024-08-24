package com.example.mvvm.datacore.token

class TokenLocalImpl(private val sharedPrefApi: TokenStoreApi) : TokenDataSource.Local {
    override fun getToken() = sharedPrefApi.getToken()

    override fun saveToken(token: String) = sharedPrefApi.saveToken(token)

    override fun clearToken() = sharedPrefApi.clearToken()
}
