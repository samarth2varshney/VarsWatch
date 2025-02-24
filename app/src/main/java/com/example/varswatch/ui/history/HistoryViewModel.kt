package com.example.varswatch.ui.history

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
class HistoryViewModel @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    private val _videoInfo = MutableLiveData<List<Item>>()
    val videoInfo: LiveData<List<Item>> = _videoInfo

    fun getHistory(){
        viewModelScope.launch {
            when(val result = repository.getHistory()){
                is Resource.Success->{
                    _videoInfo.value = result.data!!
                    Log.i("HistoryViewModel","Success ${result.data}")
                }
                is Resource.Error->{
                    Log.i("SearchViewModel","error message ${result.message}")
                }
            }
        }
    }

}