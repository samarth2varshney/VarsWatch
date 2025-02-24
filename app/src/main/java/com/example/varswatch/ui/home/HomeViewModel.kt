package com.example.varswatch.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.varswatch.domain.model.SearchResults.Item
import com.example.varswatch.domain.repository.YoutubeRepository
import com.example.varswatch.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    private val _videoInfo = MutableLiveData<List<Item>>()
    val videoInfo: LiveData<List<Item>> = _videoInfo

    fun getSubscribedChannels(){
        viewModelScope.launch {
            when(val result = repository.getSubscribedChannels()){
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    result.data?.forEach {
                        _videoInfo.value = emptyList()
                        getChannelVideos(it.id.channelId)
                    }
                }
            }
        }
    }

    private fun getChannelVideos(channelId:String){
        viewModelScope.launch {
            when(val result = repository.getChannelVideos(channelId)){
                is Resource.Success->{
                    if(result.data!=null){
                        val temp = result.data.items
                        loadExtraData(temp)
                    }
                }
                is Resource.Error->{
                    Log.i("HomeViewModel","error message ${result.message}")
                }
            }
        }

    }

    private fun loadExtraData(temp: List<Item>) {
        var ind = -1
        while (++ind<temp.size) {
            getVideoInfo(ind,temp)
        }
    }

    private fun getVideoInfo(
        ind:Int,
        temp: List<Item>
    ){

        viewModelScope.launch {
            when(val result = repository.getYoutubeData(temp[ind].id.videoId)){
                is Resource.Success->{
                    if(result.data!=null){
                        temp[ind].contentDetails = result.data.items[0].contentDetails
                        temp[ind].statistics = result.data.items[0].statistics
                        _videoInfo.value = _videoInfo.value?.plus(temp[ind])
                    }
                }
                is Resource.Error->{
                    Log.i("HomeViewModel","error message ${result.message}")
                }
            }
        }

    }

}