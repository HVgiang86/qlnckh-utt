package com.example.mvvm.di

import android.content.Context
import android.content.SharedPreferences
import com.example.mvvm.base.AppConstants
import com.example.mvvm.data.source.ProjectDataSource
import com.example.mvvm.data.source.ProjectDataSourceImpl
import com.example.mvvm.data.source.UserDataSource
import com.example.mvvm.data.source.UserDataSourceImpl
import com.example.mvvm.data.source.api.FirebaseApi
import com.example.mvvm.data.source.api.FirebaseApiImpl
import com.example.mvvm.data.source.api.MyApi
import com.example.mvvm.datacore.token.TokenDataSource
import com.example.mvvm.datacore.token.TokenLocalImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences {
        return context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFirebaseApi(): FirebaseApi {
        return FirebaseApiImpl()
    }

    @Provides
    @Singleton
    fun provideUserDataSource(myApi: MyApi): UserDataSource {
        return UserDataSourceImpl(myApi)
    }

    @Provides
    @Singleton
    fun provideProjectDataSource(myApi: MyApi): ProjectDataSource {
        return ProjectDataSourceImpl(myApi)
    }

    @Provides
    @Singleton
    internal fun provideTokenLocal(tokenStoreApi: com.example.mvvm.datacore.token.TokenStoreApi): TokenDataSource.Local {
        return TokenLocalImpl(tokenStoreApi)
    }
}
