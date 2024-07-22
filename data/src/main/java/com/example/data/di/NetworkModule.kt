package com.example.data.di

import com.example.data.constant.BASE_URL
import com.example.data.network.ServiceGenerator
import com.example.data.network.interceptor.InterceptorImpl
import com.example.data.service.ExampleApi
import com.example.data.token.TokenRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor(tokenRepository: TokenRepository): Interceptor {
        return InterceptorImpl(tokenRepository)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        logging: HttpLoggingInterceptor,
        tokenInterceptor: Interceptor,
        gson: Gson,
    ): Retrofit {
        return ServiceGenerator.generate(
            BASE_URL,
            gson,
            listOf(tokenInterceptor, logging),
        )
    }

    @Provides
    @Singleton
    fun provideApiService(
        tokenInterceptor: Interceptor,
        logging: HttpLoggingInterceptor,
        gson: Gson,
    ): ExampleApi {
        return ServiceGenerator.generate(
            BASE_URL,
            ExampleApi::class.java,
            gson,
            listOf(tokenInterceptor, logging),
        )
    }
}
