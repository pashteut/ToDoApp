package com.pashteut.todoapp.features.common.di

import android.content.Context
import com.pashteut.todoapp.features.common.UIMessagesStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FeaturesCommonModule {
    @Provides
    @Singleton
    fun provideUIMessageStorage(@ApplicationContext context: Context): UIMessagesStorage = UIMessagesStorage(context)
}