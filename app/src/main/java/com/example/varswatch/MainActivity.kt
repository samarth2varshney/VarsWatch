package com.example.varswatch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.varswatch.databinding.ActivityMainBinding
import com.example.varswatch.ui.PlayerFragment
import com.example.varswatch.util.SharedData.Array
import com.example.varswatch.util.SharedData.getVideoInfoList
import com.example.varswatch.util.SharedData.mp
import com.example.varswatch.util.getNotificationPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide();
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
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        getNotificationPermission {}

        openPlayer("jXwg9l9D51A","")

        lifecycleScope.launchWhenStarted {
            PlayerFragment.playerProgress.collect { progress ->
                binding.container.progress = progress
            }
        }

    }

    override fun onBackPressed() {

        if(PlayerFragment.playerProgress.value == 1.0f) {
            PlayerFragment.motionLayout?.transitionToStart()
            return
        }

        super.onBackPressed()
    }

    fun openPlayer(url:String,title:String){
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.playerFragmentContainer, PlayerFragment.newInstance(url,title))
//            .addToBackStack(null)
//            .commit()
        val intent = Intent(this, VideoPlayerActivity::class.java).apply {
            putExtra("youtubelink",url)
            putExtra("youtubetitle",title)
        }
        startActivity(intent)

    }

}