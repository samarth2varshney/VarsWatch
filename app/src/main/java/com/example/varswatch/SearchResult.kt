package com.example.varswatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.varswatch.ui.search.search_video_adapter
import com.example.varswatch.databinding.ActivitySearchResultBinding
import com.example.varswatch.data.SearchResultsDto
import com.example.varswatch.util.SharedData
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
                val result = gson.fromJson(responseBody!!, SearchResultsDto::class.java)
                runOnUiThread {
                    initialize(result)
                }
            } else {
                Log.i("SearchResult","failed to fetch data")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun initialize(videoInfo: SearchResultsDto) {
        val filteredList = videoInfo.items.filter { it.id.videoId != null }.toList()

        binding.searchResultRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = search_video_adapter(this, filteredList)
        binding.searchResultRecyclerView.adapter = adapter
    }

}