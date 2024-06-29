package com.mithilakshar.downloader.Utility

import android.content.Context
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
class FileDownloader(private val context: Context) {

    private val client = OkHttpClient()
    private val downloadStatus = MutableLiveData<Boolean>()
    private val downloadProgress = MutableLiveData<Int>()

    fun getDownloadStatus(): LiveData<Boolean> = downloadStatus

    fun downloadFile(url: String, fileName: String,deleteOrReturn: String) {
        val deleteIfExists = deleteOrReturn.equals("delete", ignoreCase = true)
        CoroutineScope(Dispatchers.IO).launch {
            // Create or get the "test" directory in the external storage directory
            val externalDir = File(context.getExternalFilesDir(null), "test")
            if (!externalDir.exists()) {
                externalDir.mkdirs()
            }

            if (externalDir.exists()) {
                val file = File(externalDir, fileName)

                // Check if the file already exists
                if (file.exists()) {
                    if (deleteOrReturn=="delete") {
                        file.delete()
                        println("Deleted existing file: $fileName")
                    } else {
                        println("File already exists and deleteOrReturn is 'return': $fileName")
                        downloadStatus.postValue(true)
                        return@launch
                    }
                }

                // Download the file
                val request = Request.Builder().url(url).build()
                try {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    response.body?.let { body ->
                        val inputStream: InputStream = body.byteStream()
                        val outputStream = FileOutputStream(file)
                        inputStream.use { input ->
                            outputStream.use { output ->
                                input.copyTo(output)
                            }
                        }
                        println("File downloaded successfully: $fileName")
                        downloadStatus.postValue(true)
                        return@launch
                    } ?: run {
                        println("Failed to download file: $fileName")
                        downloadStatus.postValue(false)
                        return@launch
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    downloadStatus.postValue(false)
                    return@launch
                }
            } else {
                println("Failed to create 'test' directory")
                downloadStatus.postValue(false)
                return@launch
            }
        }
    }
}