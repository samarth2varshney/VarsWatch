package com.example.varswatch

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class YoutubePlayer(
){
    lateinit var youTubePlayerView:YouTubePlayerView
    lateinit var youtubePlay:YouTubePlayer
    lateinit var youtubelink:String

    init {

        intance = this

    }

    fun play(){
        youTubePlayerView.enableBackgroundPlayback(true)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlay = youTubePlayer
                youTubePlayer.loadVideo(youtubelink, 0f)
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer,state: PlayerConstants.PlayerState) {
                super.onStateChange(youTubePlayer, state)
                State = state.toString()
            }

        })

    }


    companion object{
        lateinit var intance:YoutubePlayer
        lateinit var State:String
    }

}