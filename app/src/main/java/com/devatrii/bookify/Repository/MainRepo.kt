package com.devatrii.bookify.Repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.devatrii.bookify.Models.HomeModel
import com.devatrii.bookify.Utils.FirebaseResponse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainRepo(context: Context) {
    private val TAG = "MainRepo"
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private var databaseReference = firebaseDatabase.getReference("AppData").child("Home")
    private val homeLD = MutableLiveData<FirebaseResponse<ArrayList<HomeModel>>>()
    val homeLiveData get() = homeLD

    suspend fun getHomeData() {
        homeLiveData.postValue(FirebaseResponse.Loading())
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                if (!snapshots.exists()){
                    homeLiveData.postValue(FirebaseResponse.Error("Snapshot doesn't Exist"))
                    return
                }
                val tempList = ArrayList<HomeModel>()
                for (snap in snapshots.children) {
                    val homeModel = snap.getValue(HomeModel::class.java)
                    homeModel?.let {
                        tempList.add(homeModel)
                    }
                }
                Log.d(TAG, "onDataChange: $tempList")
                homeLiveData.postValue(FirebaseResponse.Success(tempList))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled: $error")
                homeLiveData.postValue(FirebaseResponse.Error("Something Went Wrong With Database"))
            }

        })
    }

}