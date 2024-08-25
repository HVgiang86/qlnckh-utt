package com.example.mvvm.datacore.token

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryImpl
@Inject constructor(private val local: TokenDataSource.Local) : TokenRepository {
    override fun getToken() = local.getToken()

    override fun saveToken(token: String) = local.saveToken(token)

    override fun clearToken() = local.clearToken()

    override fun getCookie(): String? = local.getCookie()

    override fun saveCookie(cookie: String) = local.saveCookie(cookie)

    override fun clearCookie() = local.clearCookie()
    override fun getLoginState() = local.getLoginState()

    override fun setLoginState(state: Boolean) = local.setLoginState(state)
}
