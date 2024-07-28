package com.example.drive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drive.databinding.ActivitySearchResultBinding
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

class SearchResult : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            search(SharedData.query)
        }

    }

    fun search(query: String?) {
        val client = OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("https://www.googleapis.com/youtube/v3/search?key=AIzaSyDayH1oCzyh6xr_DFUl3Jq2yLAFJZsB2B4&q=${query}&type=any&part=snippet&maxResults=30")
            .build()

        val call: Call = client.newCall(request)

        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val gson = Gson()
                val result = gson.fromJson(responseBody!!, SearchResults::class.java)
                runOnUiThread {
                    initialize(result)
                }
            } else {
                Log.i("samarth","failed to fetch data")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun initialize(videoInfo: SearchResults) {
        Log.i("samarth",videoInfo.items.toString())
        val filteredList = videoInfo.items.filter { it.id.videoId != null }.toList()
//        for (i in filteredList){
//            Log.i("samarth",i.id.toString())
//        }
        binding.searchResultRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = search_video_adapter(this, filteredList)
        binding.searchResultRecyclerView.adapter = adapter
    }

}