package com.devatrii.bookify.ViewModels

import androidx.lifecycle.ViewModel
import com.devatrii.bookify.Repository.MainRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repo: MainRepo) : ViewModel() {
    val homeLiveData get() = repo.homeLiveData

    fun getHomeData() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.getHomeData()
        }
    }

}