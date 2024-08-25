package com.example.mvvm.datacore.token

import javax.inject.Inject

class TokenLocalImpl
@Inject constructor(private val sharedPrefApi: TokenStoreApi) : TokenDataSource.Local {
    override fun getToken() = sharedPrefApi.getToken()

    override fun saveToken(token: String) = sharedPrefApi.saveToken(token)

    override fun clearToken() = sharedPrefApi.clearToken()
    override fun getCookie(): String? = sharedPrefApi.getCookie()

    override fun saveCookie(cookie: String) = sharedPrefApi.saveCookie(cookie)

    override fun clearCookie() = sharedPrefApi.clearCookie()
    override fun getLoginState() = sharedPrefApi.getLoginState()

    override fun setLoginState(state: Boolean) = sharedPrefApi.setLoginState(state)
}
