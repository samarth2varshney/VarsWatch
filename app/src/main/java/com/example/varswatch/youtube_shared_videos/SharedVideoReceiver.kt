package com.example.varswatch.youtube_shared_videos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.varswatch.VideoPlayerActivity
import com.example.varswatch.R

class SharedVideoReceiver : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = getIntent()
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)

        if (sharedText != null) {

            val playlist = "playlist" in sharedText

            var i = 0
            var youtubelink = ""

            while(sharedText[i]!='=') i++
            i++

            while(i<sharedText.length && sharedText[i]!='&'){
                youtubelink += sharedText[i++]
            }

            if(playlist){
                val intent2 = Intent(this, PlaylistExampleActivity::class.java)
                intent2.putExtra("youtubelink",youtubelink)
                startActivity(intent2)
            }else{
                val intent2 = Intent(this, VideoPlayerActivity::class.java)
                intent2.putExtra("youtubelink",youtubelink)
                startActivity(intent2)
            }
        }

    }
}