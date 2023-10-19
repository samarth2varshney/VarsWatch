package com.example.drive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyReciver :BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("MESSGAE")
        val youtubePlayer = youtube_player.intance.youtubePlay
        if(message!=null){
            val state = youtube_player.State
            if(state == "PLAYING")
                youtubePlayer.pause()
            else
                youtubePlayer.play()
        }
    }

}