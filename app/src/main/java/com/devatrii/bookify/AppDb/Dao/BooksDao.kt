package com.devatrii.bookify.AppDb.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.devatrii.bookify.AppDb.Entities.BooksEntity

@Dao
interface BooksDao {
    @Insert
    suspend fun insertBook(booksEntity: BooksEntity)

    @Delete
    suspend fun deleteBook(booksEntity: BooksEntity)

    @Query("Select * from books")
    suspend fun getAllBooks(): List<BooksEntity>

    @Query("Select * from books WHERE title == :bookTitle")
    suspend fun getBookByTitle(bookTitle: String): BooksEntity?
}