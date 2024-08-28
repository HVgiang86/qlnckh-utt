package com.example.mvvm.network.intercepter

import androidx.annotation.NonNull
import com.example.mvvm.datacore.token.TokenRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class InterceptorImpl
    @Inject
    constructor(
        private var tokenRepository: TokenRepository,
    ) : Interceptor {
        private var isRefreshToken = false

        @Throws(IOException::class)
        override fun intercept(
            @NonNull chain: Interceptor.Chain,
        ): Response {
            val builder = initializeHeader(chain)
            val request = builder.build()

            var response: Response
            if (request.url.toString().contains("login") || request.url.toString().contains("refresh") || request.url.toString().contains("register")) {
                val builder1 = request.newBuilder().removeHeader(HEADER_AUTH_TOKEN)
                val loginRequest = builder1.build()
                response = chain.proceed(loginRequest)
            } else {
                response = chain.proceed(request)

                if (!isRefreshToken && response.code == HttpURLConnection.HTTP_FORBIDDEN) {
                    val token = tokenRepository.getToken()
                    println("Stored Token: $token")

                    if (token.isNullOrEmpty()) {
                        val newRequest = initNewRequest(request, token)
                        response.close()
                        response = chain.proceed(newRequest)
                    }
                }
            }

            response.headers[HEADER_SET_COOKIE]?.let {
                println("Stored Cookie: $it")
                tokenRepository.clearCookie()
                tokenRepository.saveCookie(it)
            }

            return response
        }

        private fun initNewRequest(
            request: Request,
            token: String?,
        ): Request {
            val builder = request.newBuilder().removeHeader(HEADER_AUTH_TOKEN)
            token?.let {
                builder.header(HEADER_AUTH_TOKEN, "Bearer $token")
            }
            return builder.build()
        }

        private fun initializeHeader(chain: Interceptor.Chain): Request.Builder {
            val originRequest = chain.request()

            val builder = originRequest.newBuilder().method(originRequest.method, originRequest.body)

            tokenRepository.getToken()?.let {
                println("Stored Token: $it")
                builder.addHeader(HEADER_AUTH_TOKEN, "Bearer $it")
            }

            tokenRepository.getCookie()?.let {
                println("Stored Cookie: $it")
                builder.addHeader(HEADER_COOKIE, it)
            }

            return builder
        }

        companion object {
            private const val HEADER_ACCEPT = "Accept"
            private const val HEADER_CONTENT_TYPE = "Content-Type"
            private const val HEADER_COOKIE = "Cookie"
            private const val HEADER_SET_COOKIE = "Set-Cookie"
            private const val HEADER_AUTH_TOKEN = "Authorization"
        }
    }
