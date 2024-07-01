package com.pashteut.todoapp.di

import android.content.Context
import com.pashteut.todoapp.common.AppDispatchers
import com.pashteut.todoapp.model.ToDoItemDao
import com.pashteut.todoapp.model.ToDoItemsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTodoItemsDao(@ApplicationContext context: Context): ToDoItemDao = ToDoItemsDatabase.getInstance(context).toDoItemDao()


    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppDispatchers = AppDispatchers()
}