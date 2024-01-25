package com.devatrii.bookify.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devatrii.bookify.PdfActivity

class ToolsViewModelFactory(
    private val activity: PdfActivity
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ToolsViewModel(activity) as T
    }
}