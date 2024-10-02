package com.example.varswatch.notification_module

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.varswatch.R

object NotificationModule {

    fun provideNotificationBuilder(context: Context,title:String): NotificationCompat.Builder {

        val playIntent = Intent(context, MyReceiver::class.java).apply {
            action = "ACTION_PLAY"
        }
        val pauseIntent = Intent(context, MyReceiver::class.java).apply {
            action = "ACTION_PAUSE"
        }

        val playPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            playIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val pausePendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            pauseIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, "Main Channel ID")
            .setContentTitle(title)
            .setContentText("VarsWatch")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(0, "PLAY", playPendingIntent)
            .addAction(0, "PAUSE", pausePendingIntent)

    }

    fun provideNotificationManager(context: Context):NotificationManagerCompat{
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Main Channel ID",
                "Main Channel",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }

}