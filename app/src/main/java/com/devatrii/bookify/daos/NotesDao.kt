package com.devatrii.bookify.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.devatrii.bookify.entities.BookmarksEntity
import com.devatrii.bookify.entities.NotesEntity

@Dao
interface NotesDao {

    @Query("SELECT * from notes WHERE bookId == :bookId")
    suspend fun getNotes(bookId: String): List<NotesEntity>

    @Insert
    suspend fun addNote(entity: NotesEntity)

}