package com.example.mvvm.network.intercepter

import androidx.annotation.NonNull
import com.example.mvvm.datacore.token.TokenRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection

class InterceptorImpl(
    var tokenRepository: TokenRepository,
) : Interceptor {
    private var isRefreshToken = false

    @Throws(IOException::class)
    override fun intercept(
        @NonNull chain: Interceptor.Chain,
    ): Response {
        val builder = initializeHeader(chain)
        val request = builder.build()

        println("InterceptorImpl.intercept ${request.url} ${request.method} ${request.headers} ${request.body}")

        var response = chain.proceed(request)

        println("InterceptorImpl.intercept ${response.code} ${response.headers} ${response.body}")

        if (!isRefreshToken && response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            tokenRepository.getToken()?.let { token ->
                val newRequest = initNewRequest(request, token)
                response.close()
                response = chain.proceed(newRequest)
            }
        }
        return response
    }

    private fun initNewRequest(
        request: Request,
        token: String?,
    ): Request {
        val builder = request.newBuilder().removeHeader(HEADER_AUTH_TOKEN)
        token?.let {
            builder.header(HEADER_AUTH_TOKEN, "Bearer $it")
        }
        return builder.build()
    }

    private fun initializeHeader(chain: Interceptor.Chain): Request.Builder {
        val originRequest = chain.request()
        val builder = originRequest.newBuilder().header(HEADER_ACCEPT, "*/*").method(originRequest.method, originRequest.body)

        tokenRepository.getToken()?.let {
            builder.addHeader(HEADER_AUTH_TOKEN, it)
        }

        return builder
    }

    companion object {
        private const val HEADER_ACCEPT = "Accept"
        private const val HEADER_AUTH_TOKEN = "Authorization"
    }
}
