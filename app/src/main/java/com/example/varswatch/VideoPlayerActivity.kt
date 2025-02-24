package com.example.varswatch

import android.app.PictureInPictureParams
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.varswatch.data.remote.video_info
import com.example.varswatch.domain.model.SearchResults.Item
import com.example.varswatch.domain.repository.YoutubeRepository
import com.example.varswatch.notification_module.YouTubePlayerService
import com.example.varswatch.util.SharedData
import com.example.varswatch.util.SharedData.mp
import com.example.varswatch.util.SharedData.saveVideoInfoList
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var youTubePlayer: YouTubePlayer
    private lateinit var serviceIntent: Intent
    private var playerService: YouTubePlayerService? = null
    private lateinit var videoId:String

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? YouTubePlayerService.MusicBinder
            playerService = binder?.getService()
            youTubePlayer.let { playerService?.setYouTubePlayer(it) }
            Glide.with(this@VideoPlayerActivity)
                .asBitmap()
                .load("https://img.youtube.com/vi/$videoId/hqdefault.jpg")
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        playerService?.image?.value = resource
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            playerService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_ui)

        videoId = intent.getStringExtra("videoId").toString()
        val youtubeTitle = intent.getStringExtra("videoTitle")

        if(!mp.containsKey(videoId)){
            mp[videoId] = 1
            val videoInfo = video_info(videoId,youtubeTitle.toString())
            SharedData.Array.add(videoInfo)
            saveVideoInfoList(applicationContext,"history")
        }

        youTubePlayerView = findViewById(R.id.youtube_player_view3)

        serviceIntent = Intent(this, YouTubePlayerService::class.java)
        startService(serviceIntent)

        // Initialize YouTubePlayer
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@VideoPlayerActivity.youTubePlayer = youTubePlayer
                youTubePlayer.loadVideo(videoId,0f)
                bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
            }
        })

        youTubePlayerView.enableBackgroundPlayback(true)

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
        unbindService(serviceConnection)
        stopService(serviceIntent)
    }

}