package com.pashteut.todoapp.common.di

import com.pashteut.todoapp.common.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreCommonModule {
    @Provides
    @Singleton
    fun provideAppDispatcher(): AppDispatchers = AppDispatchers()
}