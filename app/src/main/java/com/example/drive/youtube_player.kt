package com.example.drive

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Button
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.*

class youtube_player(
    youTubePlayerView: YouTubePlayerView,
    youtubelink: String,
){
    var youTubePlayerVew:YouTubePlayerView
    lateinit var youtubePlay:YouTubePlayer

    init {

        intance = this

        youTubePlayerVew = youTubePlayerView
        youTubePlayerView.enableBackgroundPlayback(true)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlay = youTubePlayer
                youTubePlayer.loadVideo(youtubelink, 0f)
            }

            @SuppressLint("RemoteViewLayout")
            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer,state: PlayerConstants.PlayerState) {
                super.onStateChange(youTubePlayer, state)
                State = state.toString()
            }

        })
    }

    companion object{
        lateinit var intance:youtube_player
        lateinit var State:String
    }

}