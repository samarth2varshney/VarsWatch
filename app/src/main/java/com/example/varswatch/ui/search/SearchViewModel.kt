package com.example.varswatch.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.varswatch.data.remote.SearchResultsDto
import com.example.varswatch.data.remote.SearchResultsDto.Item
import com.example.varswatch.domain.repository.YoutubeRepository
import com.example.varswatch.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    private var _videoInfo = MutableLiveData<List<Item>>()
    val videoInfo: LiveData<List<Item>> get() = _videoInfo

    fun search(query: String) {

        viewModelScope.launch {
            when(val result = repository.getSearchResults(query)){
                is Resource.Success->{
                    if(result.data!=null){
                        val temp = result.data.items
                        _videoInfo.value = temp
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
        }
    }

    private fun getVideoInfo(
        ind:Int,
        temp: List<Item>
    ){

        viewModelScope.launch {
            when(val result = repository.getYoutubeData(temp[ind].id.videoId!!)){
                is Resource.Success->{
                    if(result.data!=null){
                        temp[ind].contentDetails = result.data.items!![0]!!.contentDetails
                        temp[ind].statistics = result.data.items[0]!!.statistics
                        _videoInfo.value = temp
                    }
                }
                is Resource.Error->{
                    Log.i("SearchViewModel","error message ${result.message}")
                }
            }
        }

    }

}