package com.example.varswatch.youtube_shared_videos

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.drive.notification_module.NotificationModule
import com.example.varswatch.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class PlaylistExampleActivity : AppCompatActivity() {
    private var youTubePlayerView: YouTubePlayerView? = null
    private var youTubePlayer: YouTubePlayer? = null
    lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManagerCompat

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_example)

        val youtubelink = intent.getStringExtra("youtubelink")
        notificationBuilder = NotificationModule.provideNotificationBuilder(applicationContext)
        notificationManager = NotificationModule.provideNotificationManager(applicationContext)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            return

        notificationManager.notify(2,notificationBuilder.build())

        youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view2).apply {
            val iFramePlayerOptions = IFramePlayerOptions.Builder()
                .controls(1)
                .listType("playlist")
                .list(youtubelink.toString())
                .build()

            lifecycle.addObserver(this)
            this.initialize(
                youtubePlayerListener,
                handleNetworkEvents = true,
                iFramePlayerOptions
            )
        }

        findViewById<Button>(R.id.next_video_button).setOnClickListener {
            //youTubePlayer?.nextVideo()
        }

        findViewById<Button>(R.id.previous_video_button).setOnClickListener {
            //youTubePlayer?.previousVideo()
        }

        findViewById<Button>(R.id.play_second_video_button).setOnClickListener {
            //youTubePlayer?.playVideoAt(1)
        }

        findViewById<Button>(R.id.shuffle_button).setOnClickListener {
            //youTubePlayer?.setShuffle(true)
        }

        findViewById<Button>(R.id.loop_button).setOnClickListener {
            //youTubePlayer?.setLoop(true)
        }
    }

    private val youtubePlayerListener = object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
            this@PlaylistExampleActivity.youTubePlayer = youTubePlayer
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancel(2)
    }
}