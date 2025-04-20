package com.eatclub.challenge.di

import com.eatclub.challenge.di.model.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Provides
    @Singleton
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchers(
            main = Dispatchers.Main,
            io = Dispatchers.IO,
            default = Dispatchers.Default,
        )
    }
}
