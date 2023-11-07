package com.example.drive

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

object SharedData {
    var observableArray: Array<video_info>? = null
    val Array: MutableLiveData<Array<video_info>> = MutableLiveData()
    var query:String = ""
}