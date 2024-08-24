package com.example.mvvm.di

import com.example.mvvm.constants.BASE_URL
import com.example.mvvm.data.source.api.MyApi
import com.example.mvvm.datacore.token.TokenRepository
import com.example.mvvm.network.ServiceGenerator
import com.example.mvvm.network.intercepter.InterceptorImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideTokenInterceptor(tokenRepository: TokenRepository): Interceptor {
        println("Request provideTokenInterceptor $tokenRepository")
        return InterceptorImpl(tokenRepository)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Provides
    @Singleton
    fun provideApiService(
        gson: Gson,
    ): MyApi {
        val logging1 = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
//        println("NetworkModule.provideApiService $tokenInterceptor")
        return ServiceGenerator.generate(
            BASE_URL,
            MyApi::class.java,
            gson,
//            listOf(tokenInterceptor, logging),
            listOf(logging, logging1),
        )
    }
}
