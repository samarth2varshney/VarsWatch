package com.example.varswatch

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.varswatch.notification_module.NotificationModule
import com.example.varswatch.util.Constants.VIDEO_PLAYER
import com.example.varswatch.util.SharedData.player
import com.example.varswatch.util.SharedData.youtubePlayers
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class YoutubePlayer(
){
    lateinit var youTubePlayerView:YouTubePlayerView
    lateinit var youtubelink:String
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManagerCompat
    lateinit var context: Context

    fun play(){

        player = VIDEO_PLAYER
        notificationBuilder = NotificationModule.provideNotificationBuilder(context)
        notificationManager = NotificationModule.provideNotificationManager(context)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManager.notify(1,notificationBuilder.build())
        }

        youTubePlayerView.enableBackgroundPlayback(true)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlayers[VIDEO_PLAYER] = youTubePlayer
                youTubePlayer.loadVideo(youtubelink, 0f)
            }

        })

    }

    fun release() {
        notificationManager.cancel(1)
        youTubePlayerView.release()
    }

}