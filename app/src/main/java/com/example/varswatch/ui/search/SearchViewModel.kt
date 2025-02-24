package com.example.varswatch.ui.search

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
class SearchViewModel @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    private var _videoInfo = MutableLiveData<List<Item>>()
    val videoInfo: LiveData<List<Item>> get() = _videoInfo

    private var count = 0

    fun saveToPlayList(playListName:String,item: Item){
        viewModelScope.launch {
            repository.addVideoToPlayList(item,playListName)
        }
    }

    fun search(query: String) {

        viewModelScope.launch {
            when(val result = repository.getSearchResults(query)){
                is Resource.Success->{
                    count = 0
                    if(result.data!=null){
                        val temp = result.data.items
                        loadExtraData(temp)
                    }
                }
                is Resource.Error->{
                    Log.i("SearchViewModel","error message ${result.message}")
                }
            }
        }

    }

    private fun loadExtraData(temp: List<Item>) {
        var ind = -1
        while (++ind<temp.size) {
            if(temp[ind].id.kind=="youtube#video")
                getVideoInfo(ind,temp)
            else
                count++
        }
    }

    private fun getVideoInfo(
        ind:Int,
        temp: List<Item>
    ){

        viewModelScope.launch {
            when(val result = repository.getYoutubeData(temp[ind].id.videoId)){
                is Resource.Success->{
                    count++
                    if(result.data!=null){
                        temp[ind].contentDetails = result.data.items[0].contentDetails
                        temp[ind].statistics = result.data.items[0].statistics
                        if(count==temp.size) {
                            _videoInfo.value = temp
                        }
                    }
                }
                is Resource.Error->{
                    Log.i("SearchViewModel","error message ${result.message}")
                }
            }
        }

    }

    fun subscribeToChannel(item: Item) {
        viewModelScope.launch {
            repository.subscribeToChannel(item)
        }
    }

}