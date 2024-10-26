package com.example.varswatch.youtube_shared_videos

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.varswatch.databinding.ActivityPlaylistBinding
import com.example.varswatch.notification_module.NotificationModule
import com.example.varswatch.util.Constants.PLAYLIST_PLAYER
import com.example.varswatch.util.SharedData.player
import com.example.varswatch.util.SharedData.youtubePlayers
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class PlaylistActivity : AppCompatActivity() {
    private var youTubePlayerView: YouTubePlayerView? = null
    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManagerCompat

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val youtubeLink = intent.getStringExtra("youtubelink")
        player = PLAYLIST_PLAYER
//        notificationBuilder = NotificationModule.provideNotificationBuilder(applicationContext,"")
//        notificationManager = NotificationModule.provideNotificationManager(applicationContext)
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
//            notificationManager.notify(1,notificationBuilder.build())
//        }

        youTubePlayerView = binding.youtubePlayerView2.apply {
            val iFramePlayerOptions = IFramePlayerOptions.Builder()
                .controls(1)
                .listType("playlist")
                .list(youtubeLink.toString())
                .build()

            lifecycle.addObserver(this)
            this.initialize(
                youtubePlayerListener,
                handleNetworkEvents = true,
                iFramePlayerOptions
            )
        }

        binding.nextVideoButton.setOnClickListener {
            youTubePlayer?.nextVideo()
        }

        binding.previousVideoButton.setOnClickListener {
            youTubePlayer?.previousVideo()
        }

    }

    private val youtubePlayerListener = object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
            this@PlaylistActivity.youTubePlayer = youTubePlayer
            youtubePlayers[PLAYLIST_PLAYER] = youTubePlayer
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancel(1)
    }
}