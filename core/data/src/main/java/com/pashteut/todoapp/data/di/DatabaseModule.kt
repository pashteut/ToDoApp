package com.pashteut.todoapp.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.pashteut.todoapp.common.AppDispatchers
import com.pashteut.todoapp.data.MyDataStore
import com.pashteut.todoapp.database.ToDoItemDao
import com.pashteut.todoapp.database.ToDoItemsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideTodoItemsDao(@ApplicationContext context: Context): ToDoItemDao =
        ToDoItemsDatabase.getInstance(context).toDoItemDao()

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context, appDispatchers: AppDispatchers) =
        MyDataStore(context.dataStore, appDispatchers)
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")