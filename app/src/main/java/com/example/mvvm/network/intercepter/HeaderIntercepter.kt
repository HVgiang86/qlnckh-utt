package com.example.mvvm.network.intercepter

import com.example.mvvm.datacore.token.TokenRepository
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class JwtAuthenticationInterceptor @Inject constructor(
    private var tokenRepository: TokenRepository,
) : Interceptor {
    private var jwtToken: String? = null

    fun setJwtToken(jwtToken: String?) {
        this.jwtToken = jwtToken
    }

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val original: Request = chain.request()

        val builder: Request.Builder = original.newBuilder()
            .header("Authorization", "Bearer $jwtToken")

        //String.format("Bearer %s", jwtToken));
        val request: Request = builder.build()
        return chain.proceed(request)
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
