package com.mithilakshar.downloader.Utility

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class AlarmHelper(val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarm(calendar: Calendar, message: String = "") {
        val futureInMillis = calendar.timeInMillis
        val intent = createAlarmIntent(message)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent)
    }

    private fun createAlarmIntent(message: String): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = "com.example.app.ALARM_ACTION" // Replace with your desired action
        intent.putExtra("message", message) // Add any additional data here
        return intent
    }
}