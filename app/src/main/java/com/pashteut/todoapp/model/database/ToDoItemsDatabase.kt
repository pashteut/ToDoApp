package com.pashteut.todoapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pashteut.todoapp.model.ToDoItem

/**
 * Abstract Room Database class for the ToDo application.
 *
 * This database holds the entities related to ToDo items and provides access to them through DAOs.
 * It is designed as a singleton to prevent having multiple instances of the database opened at the same time.
 *
 * @see ToDoItemDao Provides access to CRUD operations on ToDo items.
 */

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