package com.csappgenerator.androidnotification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat


class MyForegroundService : Service() {
    private var notificationBuilder: NotificationCompat.Builder? = null
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val contentIntentClose = createCloseIntent()
        notificationBuilder = NotificationCompat.Builder(this, "channelId")
            .setContentTitle("My Awesome notification")
            .setContentText("Don't forget to drink water")
            .setSmallIcon(R.drawable.ic_water)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_water, "CANCEL", contentIntentClose)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createCloseIntent(): PendingIntent {
        val intentClose = Intent(this, MyForegroundService::class.java)
        intentClose.action = "stop"
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getService(this, 0, intentClose, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getService(this, 0, intentClose, 0)

        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "My channel"
            val description = "Description of my channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channelId", name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager: NotificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if ("start" == intent.action) {
            startForeground(77, notificationBuilder!!.build())
        } else if ("stop" == intent.action) {
            stopForeground(true)
        }
        return START_NOT_STICKY
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}