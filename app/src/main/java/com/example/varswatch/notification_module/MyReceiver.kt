package com.example.varswatch.notification_module

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.varswatch.YoutubePlayer

class MyReceiver :BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("MESSGAE")
        val youtubePlayer = YoutubePlayer.intance.youtubePlay
        if(message!=null){
            val state = YoutubePlayer.State
            if(state == "PLAYING")
                youtubePlayer.pause()
            else
                youtubePlayer.play()
        }
    }

}