package com.example.varswatch.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.varswatch.data.SearchResultsDto
import com.example.varswatch.domain.repository.YoutubeRepository
import com.example.varswatch.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    private var _videoInfo = MutableLiveData<HashMap<String,SearchResultsDto.Item>>()
    val videoInfo: LiveData<HashMap<String,SearchResultsDto.Item>> get() = _videoInfo

    fun search(query: String) {

        viewModelScope.launch {
            when(val result = repository.getSearchResults(query)){
                is Resource.Success->{
                    if(result.data!=null){
                        _videoInfo.value = result.data.items.filter { it.id.videoId != null }.associateBy { it.id.videoId } as HashMap<String, SearchResultsDto.Item>
//                        _videoInfo.value = result.data.items.associateBy { it.id.videoId }
                    }
                }
                is Resource.Error->{
                    Log.i("SearchViewModel","error message ${result.message}")
                }
            }
        }

    }

}