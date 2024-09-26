package com.example.varswatch

import android.Manifest
import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.drive.notification_module.NotificationModule
import com.example.varswatch.data.remote.video_info
import com.example.varswatch.util.SharedData
import com.example.varswatch.util.SharedData.mp
import com.example.varswatch.util.SharedData.saveVideoInfoList

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var youtubePlayer: YoutubePlayer

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_ui)

        notificationBuilder = NotificationModule.provideNotificationBuilder(applicationContext)
        notificationManager = NotificationModule.provideNotificationManager(applicationContext)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManager.notify(1,notificationBuilder.build())
        }

        val youtubeLink = intent.getStringExtra("youtubelink").toString()
        val youtubeTitle = intent.getStringExtra("youtubetitle").toString()

        if(!mp.containsKey(youtubeLink)){
            mp[youtubeLink] = 1
            val videoInfo = video_info(youtubeLink,youtubeTitle)
            SharedData.Array.add(videoInfo)
            saveVideoInfoList(applicationContext,"history")
        }

        youtubePlayer = YoutubePlayer()

        youtubePlayer.youTubePlayerView = findViewById(R.id.youtube_player_view3)
        youtubePlayer.youtubelink = youtubeLink

        youtubePlayer.play()

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

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)

        if (!isInPictureInPictureMode) {
            notificationManager.cancel(1)
            youtubePlayer.youTubePlayerView.release()
        }

    }


    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        initPictureInPicture()
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancel(1)
        youtubePlayer.youTubePlayerView.release()
    }

}

