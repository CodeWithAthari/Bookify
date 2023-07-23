package com.devatrii.bookify.Utils

sealed class FirebaseResponse<T>(val data: T? = null, val errorMessage: String? = null,val progress: Int = 0) {
    class Loading<T>(private val mProgress:Int = 0) : FirebaseResponse<T>(progress = mProgress)
    class Success<T>(private val mData: T?) : FirebaseResponse<T>(data = mData)
    class Error<T>(private val error: String?) : FirebaseResponse<T>(errorMessage = error)
}