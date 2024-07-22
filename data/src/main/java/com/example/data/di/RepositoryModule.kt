package com.example.data.di

import com.example.data.token.TokenRepository
import com.example.data.token.TokenRepositoryImpl
import com.example.data.token.source.TokenDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    internal fun provideTokenRepository(local: TokenDataSource.Local): TokenRepository {
        return TokenRepositoryImpl(local)
    }
}
