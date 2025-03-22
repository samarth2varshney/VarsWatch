package com.example.varswatch.ui.play_list

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
class PlayListViewModel @Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    private val _listOfPlayLists = MutableLiveData<List<String>>()
    val listOfPlayLists: LiveData<List<String>> = _listOfPlayLists

    private val _videoInfo = MutableLiveData<List<Item>>()
    val videoInfo: LiveData<List<Item>> = _videoInfo

    fun getPlayLists(){
        viewModelScope.launch {
            when(val result = repository.getListOfPlayLists()) {
                is Resource.Success -> {
                    _listOfPlayLists.value = result.data!!
                }
                is Resource.Error -> {

                }
            }
        }
    }

    fun getPlayList(playlistName: String) {
        viewModelScope.launch {
            when (val result = repository.getPlayList(playlistName)) {
                is Resource.Success -> {
                    _videoInfo.value = result.data!!
                }

                is Resource.Error -> {}
            }
        }
    }

    fun addNewPlayList(playListName:String){
        viewModelScope.launch {
            when(repository.addNewPlayList(playListName)){
                is Resource.Success -> {
                    getPlayLists()
                }
                is Resource.Error ->{

                }
            }
        }
    }


}