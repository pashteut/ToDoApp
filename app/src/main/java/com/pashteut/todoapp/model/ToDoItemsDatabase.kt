package com.pashteut.todoapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToDoItem::class], version = 1)
internal abstract class ToDoItemsDatabase : RoomDatabase() {
    abstract fun toDoItemDao(): ToDoItemDao

    companion object{
        @Volatile
        private var INSTANCE: ToDoItemsDatabase? = null
        fun getInstance(context: Context): ToDoItemsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ToDoItemsDatabase::class.java,
                        "todo_items"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}