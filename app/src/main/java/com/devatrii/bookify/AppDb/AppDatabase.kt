package com.devatrii.bookify.AppDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devatrii.bookify.AppDb.Dao.BooksDao
import com.devatrii.bookify.AppDb.Entities.BooksEntity

@Database(entities = [BooksEntity::class], version = 1)

abstract class AppDatabase : RoomDatabase() {

    abstract fun booksDao(): BooksDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "Bookify_Books"
                    ).build()
                }
            }
            return INSTANCE!!
        }

    }

}