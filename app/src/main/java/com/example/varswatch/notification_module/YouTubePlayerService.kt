package com.example.varswatch.notification_module

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.varswatch.CHANNEL_ID
import com.example.varswatch.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

const val PREV = "prev"
const val NEXT = "next"
const val PLAY_PAUSE = "play_pause"

class YouTubePlayerService : Service() {

    private val binder = MusicBinder()

    private lateinit var session:MediaSessionCompat

    var image = MutableLiveData<Bitmap>()
    var duration = 0L
    var currentPos = 0L

    inner class MusicBinder : Binder() {

        fun getService() = this@YouTubePlayerService

    }

    var isPlaying = false
    private var youTubePlayer: YouTubePlayer? = null

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (intent.action) {
                PREV -> {
                }

                NEXT -> {
                }

                PLAY_PAUSE -> {
                    if(isPlaying)
                        youTubePlayer?.pause()
                    else
                        youTubePlayer?.play()
                }

                else -> {}
            }
        }

        return START_STICKY
    }

    fun updateDurations(currentPos: Long) {
        session.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_SEEK_TO
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

    fun setYouTubePlayer(player: YouTubePlayer) {
        youTubePlayer = player

        image.observeForever {
            sendNotification(0L)
        }

        player.addListener(object : AbstractYouTubePlayerListener() {
            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                isPlaying = state == PlayerConstants.PlayerState.PLAYING
                sendNotification(duration)
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
                super.onVideoDuration(youTubePlayer, duration)
                this@YouTubePlayerService.duration = duration.toLong() * 1000
                sendNotification(this@YouTubePlayerService.duration)
                updateDurations(currentPos)
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
                currentPos = second.toLong() * 1000
                updateDurations(currentPos)
            }

        })
    }

    private fun sendNotification(duration: Long) {

        session = MediaSessionCompat(this,"music").apply {
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onSeekTo(pos: Long) {
                    super.onSeekTo(pos)
                    youTubePlayer?.seekTo((pos/1000).toFloat())
                }

            })
            isActive = true
        }
        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0,1,2)
            .setMediaSession(session.sessionToken)

        session.setMetadata(
            MediaMetadataCompat.Builder()
                .putLong(
                    MediaMetadataCompat.METADATA_KEY_DURATION,
                    duration
                )
                .build()
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setStyle(style)
            .setContentTitle("track.name")
            .setContentText("track.desc")
            .addAction(R.drawable.ic_prev, "prev", createPendingIntent(PREV,0))
            .addAction(
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                "play_pause",
                createPendingIntent(PLAY_PAUSE,1)
            )
            .addAction(R.drawable.ic_next, "next", createPendingIntent(NEXT,2))
            .setSmallIcon(R.drawable.applogo)
            .setLargeIcon(image.value)
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startForeground(1, notification)
            }
        } else {
            startForeground(1, notification)
        }
    }

    private fun createPendingIntent(action: String, requestCode: Int): PendingIntent {
        val intent = Intent(this, YouTubePlayerService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}