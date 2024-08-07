package com.example.drive

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

object SharedData {
    var Array: ArrayList<video_info> = ArrayList()
    var query:String = ""
    var mp :MutableMap<String,Int> = mutableMapOf()

    // Function to save the ArrayList of VideoInfo objects to SharedPreferences
    fun saveVideoInfoList(context: Context, key: String) {
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(Array)
        editor.putString(key, json)
        editor.apply()
    }

    // Function to retrieve the ArrayList of VideoInfo objects from SharedPreferences
    fun getVideoInfoList(context: Context, key: String): ArrayList<video_info> {
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(key, null)
        val type = object : TypeToken<ArrayList<video_info>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }

}