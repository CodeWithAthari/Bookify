package com.devatrii.bookify

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devatrii.bookify.daos.BookmarksDao
import com.devatrii.bookify.daos.NotesDao
import com.devatrii.bookify.entities.BookmarksEntity
import com.devatrii.bookify.entities.NotesEntity

@Database(
    entities = [BookmarksEntity::class, NotesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookmarksDao(): BookmarksDao
    abstract fun notesDao(): NotesDao


    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "books_db"
                    ).build()
                }
            }
            return INSTANCE
        }


    }


}



















