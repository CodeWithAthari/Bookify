package com.devatrii.bookify.Repository

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.devatrii.bookify.Utils.MyResponses
import java.io.File

class BookRepo(val context: Context) {

    private val downloadLd = MutableLiveData<MyResponses<DownloadModel>>()
    val downloadLiveData get() = downloadLd
    val TAG = "Details_Activity"
    @SuppressLint("Range")
    suspend fun downloadPdf(url: String, fileName: String) {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),fileName)
        Log.i(TAG, "downloadPdf: ${file.absolutePath}")
        if (file.exists()){
            val model = DownloadModel(
                progress = 100,
                isDownloaded =true ,
                downloadId = -1,
                filePath = file.toURI().toString()
            )
            downloadLiveData.postValue(MyResponses.Success(model))
            Log.i(TAG, "downloadPdf: File Already Exist")
            return
        }

        downloadLiveData.postValue(MyResponses.Loading())

        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        val downloadRequest = DownloadManager.Request(Uri.parse(url)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            setTitle(fileName)
            setDescription("Downloading Book")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setAllowedOverRoaming(false)
            setAllowedOverMetered(true)
            setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
        }

        val downloadId = downloadManager.enqueue(downloadRequest)

        var isDownloaded = false
        var progress = 0

        while (!isDownloaded) {
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
            if (cursor.moveToNext()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                when (status) {
                    DownloadManager.STATUS_RUNNING -> {
                        val totalSize =
                            cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        if (totalSize > 0) {
                            val downloadedBytesSize =
                                cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                            progress = ((downloadedBytesSize * 100L) / totalSize).toInt()
                            downloadLiveData.postValue(MyResponses.Loading(progress))
                        }

                    }

                    DownloadManager.STATUS_FAILED -> {
                        val reason =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                        isDownloaded = true
                        downloadLiveData.postValue(MyResponses.Error("Failed to Download $fileName.\nReason $reason"))

                    }

                    DownloadManager.STATUS_SUCCESSFUL -> {
                        progress = 100
                        isDownloaded = true

                        val filePath =
                            cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                        val model = DownloadModel(
                            progress = progress,
                            isDownloaded = isDownloaded,
                            downloadId = downloadId,
                            filePath = filePath
                        )
                        downloadLiveData.postValue(MyResponses.Success(model))

                    }

                    DownloadManager.STATUS_PENDING -> {
                        downloadLiveData.postValue(MyResponses.Loading(progress))
                        Log.i(TAG, "downloadPdf: Pending")
                    }

                    DownloadManager.STATUS_PAUSED -> {
                        downloadLiveData.postValue(MyResponses.Loading(progress))
                        Log.i(TAG, "downloadPdf: Paused")
                    }


                }


            }
        }

    }

    data class DownloadModel(
        var progress: Int = 0,
        var isDownloaded: Boolean,
        var downloadId: Long,
        var filePath: String
    )
}