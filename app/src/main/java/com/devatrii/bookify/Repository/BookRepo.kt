package com.devatrii.bookify.Repository

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.devatrii.bookify.AppDb.AppDatabase
import com.devatrii.bookify.AppDb.Entities.BooksEntity
import com.devatrii.bookify.Utils.FirebaseResponse
import java.io.File

class BookRepo(val context: Context) {
    private val TAG = "BookRepo"
    private val downloadLD = MutableLiveData<FirebaseResponse<DownloadModel>>()
    val downloadLiveData get() = downloadLD
    private val database = AppDatabase.getDatabase(context)
    private val booksDao = database.booksDao()

    @SuppressLint("Range")
    suspend fun downloadPDF(link: String, title: String) {

        // checking in database
        val result = booksDao.getBookByTitle(title)
        result?.let {
            val downloadModel = DownloadModel(
                isDownloaded = true,
                downloadId = it.downloadId,
                filePath = Uri.parse(it.bookPDF),
                progress = 100
            )
            downloadLiveData.postValue(FirebaseResponse.Success(downloadModel))
            Log.i(TAG, "downloadPDF: Returning From Database")
            return
        }

        val fileName = "${title}"

        val request = DownloadManager.Request(Uri.parse(link)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setTitle(fileName)
            setDescription("Downloading Book")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
            setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
        }
        Log.i(TAG, "downloadPDF: Downloading")
        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)


        var isDownloaded = false
        val downloadModel = DownloadModel(
            isDownloaded = false,
            downloadId = downloadId,
            filePath = Uri.parse("")
        )
        var progress = 0
        downloadLiveData.postValue(FirebaseResponse.Loading(progress))
        while (!isDownloaded) {
//            Log.i(TAG, "downloadPDF: isDownloaded $isDownloaded")
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
            if (cursor.moveToNext()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_RUNNING -> {
                        val totalSize =
                            cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        if (totalSize > 0) {
                            val downloadedBytes =
                                cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                            progress = ((downloadedBytes * 100L) / totalSize).toInt();
                            downloadLiveData.postValue(FirebaseResponse.Loading(progress))
                        }

                    }

                    DownloadManager.STATUS_FAILED -> {
                        isDownloaded = true
                        val reason =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                        downloadLiveData.postValue(
                            FirebaseResponse.Error(
                                "Failed to Download ${
                                    fileName.split(
                                        "."
                                    )[0]
                                } Reason $reason"
                            )
                        )
                        Log.e(TAG, "downloadPDF: Failed to Download Reason $reason")
                    }

                    DownloadManager.STATUS_PAUSED -> {
                        Log.i(TAG, "downloadPDF: Downloading Paused")
                    }

                    DownloadManager.STATUS_PENDING -> {
                        Log.i(TAG, "downloadPDF: Downloading Pending")
                    }


                    DownloadManager.STATUS_SUCCESSFUL -> {
                        progress = 100
                        downloadModel.progress = progress
                        isDownloaded = true
                        Log.i(TAG, "downloadPDF: Downloaded")
                        val filePath =
                            cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                        Log.i("BookRepo", "downloadPDF: $filePath")
                        downloadModel.isDownloaded = true
                        downloadModel.filePath = Uri.parse(filePath)
                        downloadLiveData.postValue(FirebaseResponse.Success(downloadModel))
                        // saving into database
                        val booksEntity = BooksEntity(
                            id = 0,
                            bookPDF = downloadModel.filePath.toString(),
                            title = title,
                            downloadId = downloadId
                        )
                        booksDao.insertBook(booksEntity)
                        Log.i(TAG, "downloadPDF: $fileName Saved in database")
                    }
                }

            }
        }

    }
}

data class DownloadModel(
    var progress: Int = 0,
    var isDownloaded: Boolean,
    var downloadId: Long,
    var filePath: Uri,
)

