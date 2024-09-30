package com.example.varswatch.notification_module

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.varswatch.util.SharedData.isPlaying
import com.example.varswatch.util.SharedData.player
import com.example.varswatch.util.SharedData.youtubePlayers

class MyReceiver :BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val player = player
        val youtubePlayer = youtubePlayers[player]
        if(youtubePlayer!=null){
            if(isPlaying)
                youtubePlayer.pause()
            else
                youtubePlayer.play()
            isPlaying = !isPlaying
        }
    }

}