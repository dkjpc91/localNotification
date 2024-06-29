package com.mithilakshar.downloader.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mithilakshar.downloader.Utility.FileDownloader

class FileDownloadViewModel : ViewModel() {

    private val _downloadStatus = MutableLiveData<Boolean>()
    val downloadStatus: LiveData<Boolean>
        get() = _downloadStatus

    private lateinit var fileDownloader: FileDownloader

    fun startDownload(context: Context, url: String, fileName: String, deleteOrReturn: String) {
        fileDownloader = FileDownloader(context)
        fileDownloader.getDownloadStatus().observeForever { status ->
            _downloadStatus.postValue(status)
        }
        fileDownloader.downloadFile(url, fileName, deleteOrReturn)
    }
}