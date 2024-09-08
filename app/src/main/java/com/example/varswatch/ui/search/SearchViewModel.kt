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

    private var _searchResults = MutableLiveData<List<SearchResultsDto.Item>>()

    // Expose the list as LiveData
    val searchResults: LiveData<List<SearchResultsDto.Item>> get() = _searchResults

    fun search(query: String) {

        viewModelScope.launch {
            when(val result = repository.getSearchResults(query)){
                is Resource.Success->{
                    if(result.data!=null){
                        Log.i("samarth","search called")
                        _searchResults.value = result.data.items.filter { it.id.videoId != null }.toList()
                    }
                }
                is Resource.Error->{
                    Log.i("samarth","error message ${result.message}")
                }
            }
        }

    }

}