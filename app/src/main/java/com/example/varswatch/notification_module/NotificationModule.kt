package com.example.varswatch.notification_module

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.varswatch.R

object NotificationModule {

    private var songController: SongController? = null

    private lateinit var session:MediaSessionCompat

    private lateinit var notificationManager:NotificationManagerCompat

    fun mediaAction(action: String){

        when (action) {
            PREV -> songController?.previous()
            NEXT -> songController?.next()
            PLAY_PAUSE -> songController?.playPause()
            "" -> {}
        }

    }

    fun sendNotification(context: Context,songController: SongController,music: MusicState){
        this.songController = songController
        session = MediaSessionCompat(context,"music")
        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0,1,2)
            .setMediaSession(session.sessionToken)

        session.setMetadata(
            MediaMetadataCompat.Builder()
                .putLong(
                    MediaMetadataCompat.METADATA_KEY_DURATION,
                    music.duration
                )
                .build()
        )

        val notification = NotificationCompat.Builder(context, "Main Channel ID")
            .setStyle(style)
            .setContentTitle("hello testing")
            .setContentText("track.desc")
            .addAction(R.drawable.ic_prev,"prev",createPendingIntent(context, PREV,0))
            .addAction(
                if(music.isPlaying)
                    R.drawable.ic_pause
                else
                    R.drawable.ic_play,
                "play_pause",createPendingIntent(context, PLAY_PAUSE,1))
            .addAction(R.drawable.ic_next,"next",createPendingIntent(context, NEXT,2))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(music.albumArt)
            .build()

        if(ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManager = provideNotificationManager(context)
            notificationManager.notify(1,notification)
        }

    }

    private fun provideNotificationManager(context: Context): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Main Channel ID",
                "Main Channel",
                NotificationManager.IMPORTANCE_LOW,
            )
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }

    private fun createPendingIntent(context: Context,action:String,requestCode:Int): PendingIntent {
        val intent = Intent(context,MediaPlayerReceiver::class.java).apply {
            this.action = action
        }
        return PendingIntent.getBroadcast(
            context,requestCode,intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    fun updateMediaPlaybackState(currentPos: Long) {
        session.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY
                            or PlaybackStateCompat.ACTION_PLAY_PAUSE
                            or PlaybackStateCompat.ACTION_PAUSE
                            or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                            or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                            or PlaybackStateCompat.ACTION_SEEK_TO
                )
                .setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    currentPos,
                    1f,
                    SystemClock.elapsedRealtime()
                )
                .build()
        )
    }

    fun release() {
        notificationManager.cancel(1)
    }

}