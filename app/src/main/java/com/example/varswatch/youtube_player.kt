package com.example.varswatch

import android.content.Context
import android.net.wifi.WifiManager
import android.widget.ImageView
import androidx.core.content.ContextCompat.getSystemService
import com.example.varswatch.notification_module.MusicState
import com.example.varswatch.notification_module.NotificationModule
import com.example.varswatch.notification_module.SongController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class YoutubePlayer {
    lateinit var youTubePlayerView:YouTubePlayerView
    lateinit var youtubeLink:String
    var title:String = "Video"
    var currentMusicState:MusicState? = null
    lateinit var context: Context
    var youtubePlayer: YouTubePlayer? = null
    var playImageView: ImageView? = null

    private val songController = object : SongController {
        override fun playPause() {
            if (currentMusicState!!.isPlaying){
                youtubePlayer!!.pause()
            }else{
                youtubePlayer!!.play()
            }
        }

        override fun next() {
            youtubePlayer!!.nextVideo()
        }

        override fun previous() {
            youtubePlayer!!.previousVideo()
        }

        override fun stop() {

        }
    }

    fun play(currentMusic: MusicState){

        playImageView?.setOnClickListener {
            if(currentMusicState?.isPlaying==true){
                youtubePlayer?.pause()
            }else{
                youtubePlayer?.play()
            }
        }

        youTubePlayerView.enableBackgroundPlayback(true)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlayer = youTubePlayer
                youTubePlayer.loadVideo(youtubeLink, 0f)
                currentMusicState = currentMusic
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
                super.onVideoDuration(youTubePlayer, duration)
                currentMusicState = currentMusicState!!.copy(duration = duration.toLong() * 1000)
                NotificationModule.sendNotification(context,songController,currentMusicState!!)
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
                currentMusicState = currentMusicState!!.copy(currentDuration = second.toLong() * 1000)
                NotificationModule.updateMediaPlaybackState(currentMusicState!!.currentDuration)
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)
                currentMusicState = currentMusicState!!.copy(isPlaying = state == PlayerConstants.PlayerState.PLAYING)
                NotificationModule.sendNotification(context,songController,currentMusicState!!)
                changeButton()
            }

        })

    }

    fun changeButton(){
        if(currentMusicState?.isPlaying==true){
            playImageView?.setBackgroundResource(R.drawable.ic_pause)
        }else{
            playImageView?.setBackgroundResource(R.drawable.ic_play)
        }
    }

    fun release() {
        NotificationModule.release()
        youTubePlayerView.release()
    }

}