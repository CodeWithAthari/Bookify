package com.devatrii.bookify

import android.app.Application
import com.devatrii.bookify.Utils.loadAdUnits
import com.devatrii.bookify.Utils.loadInterstitialAdIfNull
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.FirebaseDatabase

class MyBookApp() : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {
            loadAdUnits {
                loadInterstitialAdIfNull(this)
            }
        }
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}