package com.example.varswatch

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.varswatch.data.remote.video_info
import com.example.varswatch.notification_module.MusicState
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
        val youtubeTitle = intent.getStringExtra("youtubetitle")

        if(!mp.containsKey(youtubeLink)){
            mp[youtubeLink] = 1
            val videoInfo = video_info(youtubeLink,youtubeTitle.toString())
            SharedData.Array.add(videoInfo)
            saveVideoInfoList(applicationContext,"history")
        }

        youtubePlayer = YoutubePlayer()

        youtubePlayer.context = this
        youtubePlayer.youTubePlayerView = findViewById(R.id.youtube_player_view3)
        youtubePlayer.youtubeLink = youtubeLink
        if (youtubeTitle != null) {
            youtubePlayer.title = youtubeTitle
        }

        Glide.with(this)
            .asBitmap()
            .load("https://img.youtube.com/vi/$youtubeLink/hqdefault.jpg")
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    youtubePlayer.play(
                        MusicState(
                            isPlaying = true,
                            title = title.toString(),
                            artist = "LE SSERAFIM",
                            album = "ANTIFRAGILE",
                            albumArt = resource,
                            duration = 0
                        )
                    )
                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })

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