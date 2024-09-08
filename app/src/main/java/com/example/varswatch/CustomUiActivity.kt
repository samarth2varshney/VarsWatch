package com.example.varswatch

import android.Manifest
import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.drive.notification_module.NotificationModule
import com.example.varswatch.util.SharedData
import com.example.varswatch.util.SharedData.mp
import com.example.varswatch.util.SharedData.saveVideoInfoList
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class CustomUiActivity : AppCompatActivity() {

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var youTubePlayerView:YouTubePlayerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_ui)

        notificationBuilder = NotificationModule.provideNotificationBuilder(applicationContext)
        notificationManager = NotificationModule.provideNotificationManager(applicationContext)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            return

        notificationManager.notify(1,notificationBuilder.build())

        youTubePlayerView = findViewById(R.id.youtube_player_view)
        val youtubelink = intent.getStringExtra("youtubelink").toString()
        val youtubeTitle = intent.getStringExtra("youtubetitle").toString()

        if(!mp.containsKey(youtubelink)){
            mp[youtubelink] = 1
            val videoInfo = video_info(youtubelink,youtubeTitle)
            SharedData.Array.add(videoInfo)
            saveVideoInfoList(applicationContext,"history")
        }

        YoutubePlayer(youTubePlayerView,youtubelink)
    }

    private fun initPictureInPicture() {
        val supportsPIP: Boolean = packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && supportsPIP) {
            val params = PictureInPictureParams.Builder().build()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                enterPictureInPictureMode(params)
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        initPictureInPicture()
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancel(1)
    }

}

