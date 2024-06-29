package com.mithilakshar.downloader

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class NotificationHelper(private val context: Context, @DrawableRes private val defaultIcon: Int) { // Allow customization of default icon

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val smallIcon: Int

    init {
        this.smallIcon = defaultIcon
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    private fun createNotificationChannel() {
        val channelId = "your_channel_id"
        val channelName = "Your Channel Name"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)
        // Configure the channel (optional): description, sound, vibration etc.

        notificationManager.createNotificationChannel(channel)
    }

    fun createNotificationWithImage(
        imageUrl: String,
        title: String,
        message: String,
        activity: Class<out Activity>,
        @DrawableRes smallIcon: Int = defaultIcon // Use default or provided icon
    ) {
        val intent = Intent(context, activity)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val rawSoundUri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.ram)

                    val builder = NotificationCompat.Builder(context, "your_channel_id")
                        .setSmallIcon(smallIcon)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                    builder.setSound(null)

                    notificationManager.notify(NOTIFICATION_ID, builder.build())

                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(context, rawSoundUri)
                    mediaPlayer.setOnCompletionListener {
                        mediaPlayer.release() // Release the MediaPlayer after completion
                    }
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    // Handle loading failure (optional)
                    createNotification(title, "Failed to load image", activity)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle resource cleared (optional)
                }
            })
    }

    private fun createNotification(title: String, message: String, activity: Class<out Activity>) {
        val intent = Intent(context, activity)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, "your_channel_id")
            .setSmallIcon(smallIcon) // Use default icon here
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val rawSoundId = R.raw.ram // Replace with your raw resource name
        val mediaPlayer = MediaPlayer.create(context, rawSoundId)
        mediaPlayer.setOnCompletionListener {
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
        mediaPlayer.start()

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        private const val NOTIFICATION_ID = 100
    }
}
