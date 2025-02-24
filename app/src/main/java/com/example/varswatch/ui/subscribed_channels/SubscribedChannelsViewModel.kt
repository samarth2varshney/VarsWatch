package com.example.varswatch.ui.subscribed_channels

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
class SubscribedChannelsViewModel@Inject constructor(
    private val repository: YoutubeRepository
) : ViewModel() {

    private val _channelsList = MutableLiveData<List<Item>>()
    val channelsList: LiveData<List<Item>> = _channelsList

    fun getSubscribedChannels(){
        viewModelScope.launch {
            when(val result = repository.getSubscribedChannels()){
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    _channelsList.value = result.data!!
                }
            }
        }
    }

    fun unsubscribeToChannel(item: Item){
        viewModelScope.launch {
            when(repository.unsubscribeToChannel(item)){
                is Resource.Error ->{}
                is Resource.Success ->{
                    getSubscribedChannels()
                }
            }
        }
    }

}