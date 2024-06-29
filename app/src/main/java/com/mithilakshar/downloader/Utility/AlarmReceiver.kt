package com.mithilakshar.downloader.Utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.mithilakshar.downloader.MainActivity
import com.mithilakshar.downloader.NotificationHelper
import com.mithilakshar.downloader.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        // Do your task here, like showing a toast
        Toast.makeText(context, "Alarm Triggered!", Toast.LENGTH_SHORT).show()
        val notificationHelper = NotificationHelper(context, R.drawable.ic_launcher_background)
        val imageUrl = "https://i.pinimg.com/564x/44/ca/c9/44cac9ad222f947f6f128b6491c009a2.jpg"
        val title = "Your Notification Title"
        val message = "This is the notification message"
        val activityToLaunch = MainActivity::class.java
        notificationHelper.createNotificationWithImage(imageUrl, title, message, activityToLaunch)


    }
}