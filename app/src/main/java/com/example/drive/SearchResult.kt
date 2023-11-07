package com.example.drive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
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
            makeApiCall(SharedData.query)
        }

    }

    fun makeApiCall(query: String?) {
        val client = OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("http://192.168.137.42:8000/api/hello/?id=${query}")
            .build()

        val call: Call = client.newCall(request)

        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val gson = Gson()
                val stringArray: Array<video_info> = gson.fromJson(responseBody!!, Array<video_info>::class.java)
                runOnUiThread{
                    initialize(stringArray)
                }
            } else {
                Log.i("samarth","failed to fetch data")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun initialize(videoInfo: Array<video_info>) {
        binding.loader.visibility = View.GONE
        binding.searchResultRecyclerView.layoutManager = LinearLayoutManager(this)
        val arrayList = videoInfo!!.toList() as ArrayList<video_info>
        val adapter = search_video_adapter(this, arrayList)
        binding.searchResultRecyclerView.adapter = adapter
    }

}