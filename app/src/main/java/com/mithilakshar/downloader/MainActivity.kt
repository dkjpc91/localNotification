package com.mithilakshar.downloader

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mithilakshar.downloader.Utility.FileDownloaderProgress
import com.mithilakshar.downloader.ViewModel.FileDownloadViewModel
import com.mithilakshar.downloader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val viewModel: FileDownloadViewModel by viewModels()



    val fileUrl = "https://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Sevish_-__nbsp_.mp3"
    val fileName = "file.mp3"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val fileDownloader = FileDownloaderProgress(this)


        var toast: Toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)




        fileDownloader.getDownloadStatus().observe(this, { isSuccess ->
            if (isSuccess) {
               toast.setText("Download successful!")
               toast.show()
               binding.textViewProgress.text="Download successful"
            } else {
                toast.setText("Download xxx!")
                toast.show()
               binding.textViewProgress.text="Download xxxxx"
            }
        })

        fileDownloader.getDownloadProgress().observe(this, { progress ->
            // Update UI with download progress (progress is a value between 0 and 100)
            binding.textViewProgress.text="Download $progress"
        })

        // Start file download
        fileDownloader.downloadFile(fileUrl, fileName, "return")

        val notificationHelper = NotificationHelper(this, R.drawable.ic_launcher_background)
        val imageUrl = "https://i.pinimg.com/564x/44/ca/c9/44cac9ad222f947f6f128b6491c009a2.jpg"
        val title = "Your Notification Title"
        val message = "This is the notification message"
        val activityToLaunch = MainActivity::class.java

        binding.button.setOnClickListener {

            notificationHelper.createNotificationWithImage(imageUrl, title, message, activityToLaunch)


        }











        /* viewModel.downloadStatus.observe(this, Observer { isDownloaded ->
             if (isDownloaded) {

                 Glide.with(this)
                     .load(File(this.getExternalFilesDir("test"), "file.jpeg"))
                     .into(binding.image)

                 Toast.makeText(this, "File is downloaded and saved as $fileName in 'test' directory", Toast.LENGTH_LONG).show()
             } else {
                 Toast.makeText(this, "Failed to download the file", Toast.LENGTH_LONG).show()
             }
         })

         // Start the download
         viewModel.startDownload(this, fileUrl, fileName,"delete")*/



    }
}