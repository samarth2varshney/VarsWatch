package com.example.varswatch

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.varswatch.databinding.ActivityMainBinding
import com.example.varswatch.domain.model.SearchResults.Item
import com.example.varswatch.domain.repository.YoutubeRepository
import com.example.varswatch.util.SharedData.Array
import com.example.varswatch.util.SharedData.getVideoInfoList
import com.example.varswatch.util.SharedData.mp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var repository: YoutubeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        Array = getVideoInfoList(applicationContext,"history")

        mp["videoID"] = 1

        for (video in Array) {
            mp[video.video_id] = 1
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_subscribed_channels, R.id.navigation_playList
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    fun openPlayer(item: Item){

        addToHistory(item)
        val intent = Intent(this, VideoPlayerActivity::class.java).apply {
            putExtra("videoId",item.id.videoId)
            putExtra("videoTitle",item.snippet.title)
        }
        startActivity(intent)
    }

    private fun addToHistory(item: Item){
        CoroutineScope(Dispatchers.Main).launch {
            repository.addVideoToHistory(item)
        }
    }

}