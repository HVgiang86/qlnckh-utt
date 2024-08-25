package com.example.mvvm.di

import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.data.UserRepository
import com.example.mvvm.data.repository.ProjectRepositoryImpl
import com.example.mvvm.data.repository.UserRepositoryImpl
import com.example.mvvm.data.source.ProjectDataSource
import com.example.mvvm.data.source.UserDataSource
import com.example.mvvm.datacore.token.TokenDataSource
import com.example.mvvm.datacore.token.TokenRepository
import com.example.mvvm.datacore.token.TokenRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideProjectRepository(remote: ProjectDataSource): ProjectRepository {
        return ProjectRepositoryImpl(remote)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(userDataSource)
    }

    @Provides
    @Singleton
    internal fun provideTokenRepository(local: TokenDataSource.Local): TokenRepository {
        return TokenRepositoryImpl(local)
    }
}
