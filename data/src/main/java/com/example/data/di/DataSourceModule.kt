package com.example.data.di

import com.example.data.datastore.TokenStoreApi
import com.example.data.token.source.TokenDataSource
import com.example.data.token.source.TokenLocalImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    @Provides
    @Singleton
    internal fun provideTokenLocal(tokenStoreApi: TokenStoreApi): TokenDataSource.Local {
        return TokenLocalImpl(tokenStoreApi)
    }
}
