package com.example.mvvm.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class DispatcherModule {
    @Provides
    @Singleton
    internal fun provideDefaultDispatcher(): DefaultDispatcher {
        return DefaultDispatcher()
    }

    @Provides
    @Singleton
    internal fun provideIODispatcher(): IODispatcher {
        return IODispatcher()
    }

    @Provides
    @Singleton
    internal fun provideMainDispatcher(): MainDispatcher {
        return MainDispatcher()
    }

    @Provides
    @Singleton
    internal fun provideUnconfinedDispatcher(): UnconfinedDispatcher {
        return UnconfinedDispatcher()
    }
}

interface DispatchersProvider {
    fun dispatcher(): CoroutineContext
}

class DefaultDispatcher : DispatchersProvider {
    override fun dispatcher() = Dispatchers.Default
}

class IODispatcher : DispatchersProvider {
    override fun dispatcher() = Dispatchers.IO
}

class MainDispatcher : DispatchersProvider {
    override fun dispatcher() = Dispatchers.Main
}

class UnconfinedDispatcher : DispatchersProvider {
    override fun dispatcher() = Dispatchers.Unconfined
}
