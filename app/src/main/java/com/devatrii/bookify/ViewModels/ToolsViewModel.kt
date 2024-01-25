package com.devatrii.bookify.ViewModels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.devatrii.bookify.AppDatabase
import com.devatrii.bookify.PdfActivity
import com.devatrii.bookify.entities.BookmarksEntity
import com.devatrii.bookify.entities.NotesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("ALL")
class ToolsViewModel(val pdfActivity: PdfActivity) : ViewModel() {

    private var nightMode = false
    val database = AppDatabase.getDatabase(pdfActivity)

    fun toggleNightMode() {
        nightMode = !nightMode
        pdfActivity.binding.pdfView.setNightMode(nightMode)

    }

    fun jumpToPage(pageNo: Int) {
        pdfActivity.binding.pdfView.jumpTo(pageNo, true)
    }

    fun addBookmark() {
        CoroutineScope(Dispatchers.IO).launch {
            val entity =
                BookmarksEntity(0, pdfActivity.bookId, pdfActivity.binding.pdfView.currentPage)
            database?.bookmarksDao()?.addBookmark(entity)
        }
    }

    fun addNote(note: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val entity = NotesEntity(0, pdfActivity.bookId, note)
            database?.notesDao()?.addNote(entity)

        }
    }


}



















