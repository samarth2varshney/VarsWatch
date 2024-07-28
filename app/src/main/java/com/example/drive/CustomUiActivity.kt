package com.example.drive

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.drive.SharedData.mp
import com.example.drive.SharedData.saveVideoInfoList
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.*

class CustomUiActivity : AppCompatActivity() {

    lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManagerCompat

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_ui)

        notificationBuilder = NotificationModule.provideNotificationBuilder(applicationContext)
        notificationManager = NotificationModule.provideNotificationManager(applicationContext)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            return

        notificationManager.notify(1,notificationBuilder.build())

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        val youtubelink = intent.getStringExtra("youtubelink").toString()
        val youtubeTitle = intent.getStringExtra("youtubetitle").toString()

        if(!mp.containsKey(youtubelink)){
            mp[youtubelink] = 1
            val videoInfo = video_info(youtubelink,youtubeTitle)
            SharedData.Array.add(videoInfo)
            Log.i("samarth","chala")
            saveVideoInfoList(applicationContext,"history")
        }

        youtube_player(youTubePlayerView,youtubelink)
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancel(1)
    }

}

