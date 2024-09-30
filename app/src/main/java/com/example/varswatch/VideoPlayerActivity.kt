package com.example.varswatch

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.example.varswatch.data.remote.video_info
import com.example.varswatch.util.SharedData
import com.example.varswatch.util.SharedData.mp
import com.example.varswatch.util.SharedData.saveVideoInfoList

class VideoPlayerActivity : AppCompatActivity() {
    
    private lateinit var youtubePlayer: YoutubePlayer

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_ui)

        val youtubeLink = intent.getStringExtra("youtubelink").toString()
        val youtubeTitle = intent.getStringExtra("youtubetitle").toString()

        if(!mp.containsKey(youtubeLink)){
            mp[youtubeLink] = 1
            val videoInfo = video_info(youtubeLink,youtubeTitle)
            SharedData.Array.add(videoInfo)
            saveVideoInfoList(applicationContext,"history")
        }

        youtubePlayer = YoutubePlayer()

        youtubePlayer.context = this
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
        if (lifecycle.currentState == Lifecycle.State.CREATED) {
            youtubePlayer.release()
            finishAndRemoveTask()
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    }


    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        initPictureInPicture()
    }

    override fun onDestroy() {
        super.onDestroy()
        youtubePlayer.release()
    }

}