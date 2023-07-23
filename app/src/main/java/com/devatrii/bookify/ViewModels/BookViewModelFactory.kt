package com.devatrii.bookify.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devatrii.bookify.Repository.BookRepo
import com.devatrii.bookify.Repository.MainRepo

class BookViewModelFactory(private val repository: BookRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookViewModel(repository) as T
    }
}