package com.example.data.network.interceptor

import androidx.annotation.NonNull
import com.example.data.token.TokenRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection

class InterceptorImpl(
    private var tokenRepository: TokenRepository,
) : Interceptor {
    private var isRefreshToken = false

    @Throws(IOException::class)
    override fun intercept(
        @NonNull chain: Interceptor.Chain,
    ): Response {
        val builder = initializeHeader(chain)
        val request = builder.build()
        var response = chain.proceed(request)

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
            builder.header(HEADER_AUTH_TOKEN, it)
        }
        return builder.build()
    }

    private fun initializeHeader(chain: Interceptor.Chain): Request.Builder {
        val originRequest = chain.request()
        val builder = originRequest.newBuilder().header(HEADER_ACCEPT, "application/json").method(originRequest.method, originRequest.body)

        tokenRepository.getToken()?.let {
            builder.addHeader(HEADER_AUTH_TOKEN, it)
        }

        return builder
    }

    companion object {
        private const val HEADER_ACCEPT = "Accept"
        private const val HEADER_AUTH_TOKEN = "Bearer"
    }
}
