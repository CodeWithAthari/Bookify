package com.devatrii.bookify

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MyBookApp():Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}