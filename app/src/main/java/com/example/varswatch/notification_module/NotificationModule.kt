package com.example.drive.notification_module

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.varswatch.R
import com.example.varswatch.notification_module.MyReceiver

object NotificationModule {

    fun provideNotificationBuilder( context: Context): NotificationCompat.Builder{

        val intent = Intent(context, MyReceiver::class.java).apply {
            putExtra("MESSGAE", "PLAY")
        }
        val flag = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            PendingIntent.FLAG_IMMUTABLE
        }else{
            0
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            flag,
        )

        return NotificationCompat.Builder(context,"Main Channel ID")
            .setContentTitle("watch")
            .setContentText("Youtube channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(0,"PLAY/PAUSE",pendingIntent)
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