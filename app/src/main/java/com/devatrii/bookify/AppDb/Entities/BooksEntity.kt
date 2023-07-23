package com.devatrii.bookify.AppDb.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BooksEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bookPDF:String,
    val title:String,
    val downloadId:Long,
)