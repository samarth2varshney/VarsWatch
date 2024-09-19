package com.example.varswatch.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.varswatch.data.remote.SearchResultsDto
import com.example.varswatch.domain.repository.YoutubeRepository
import com.example.varswatch.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    private var _videoInfo = MutableLiveData<HashMap<String, SearchResultsDto.Item>>()
    val videoInfo: LiveData<HashMap<String, SearchResultsDto.Item>> get() = _videoInfo

    fun search(query: String) {

        viewModelScope.launch {
            when(val result = repository.getSearchResults(query)){
                is Resource.Success->{
                    if(result.data!=null){
                        val temp = result.data.items.filter { it.id.videoId != null }.associateBy { it.id.videoId } as HashMap<String, SearchResultsDto.Item>
                        loadExtraData(temp)
                    }
                }
                is Resource.Error->{
                    Log.i("SearchViewModel","error message ${result.message}")
                }
            }
        }

    }

    private fun loadExtraData(temp: HashMap<String, SearchResultsDto.Item>) {
        val str: HashMap<String, SearchResultsDto.Item> = hashMapOf()
        temp.forEach {
            getVideoInfo(it.key,temp,str)
        }
    }

    private fun getVideoInfo(
        id: String,
        temp: HashMap<String, SearchResultsDto.Item>,
        str: HashMap<String, SearchResultsDto.Item>
    ){

        viewModelScope.launch {
            when(val result = repository.getYoutubeData(id)){
                is Resource.Success->{
                    if(result.data!=null){
                        temp[id]!!.contentDetails = result.data.items!![0]!!.contentDetails
                        temp[id]!!.statistics = result.data.items[0]!!.statistics
                        str[id] = temp[id]!!
                        if(str.size==temp.size)
                            _videoInfo.value = str
                    }
                }
                is Resource.Error->{
                    Log.i("SearchViewModel","error message ${result.message}")
                }
            }
        }

    }

}