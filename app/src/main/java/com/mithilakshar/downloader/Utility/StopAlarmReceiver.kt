package com.mithilakshar.downloader.Utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mithilakshar.downloader.NotificationHelper

class StopAlarmReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "STOP_AUDIO_ACTION") {
                // Stop and release MediaPlayer instance here
                NotificationHelper.mediaPlayer?.stop()
                NotificationHelper.mediaPlayer?.release()
                NotificationHelper.mediaPlayer = null
            }
        }
 }