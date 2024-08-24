package com.example.mvvm.di

import android.content.Context
import com.example.mvvm.datacore.token.TokenStoreApi
import com.example.mvvm.datacore.token.TokenStoreApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalTokenModule {
    @Provides
    @Singleton
    internal fun provideTokenLocalApi(
        @ApplicationContext context: Context,
    ): TokenStoreApi {
        return TokenStoreApiImpl(context)
    }
}
