package com.example.varswatch.domain.repository

import com.example.varswatch.domain.model.SearchResults
import com.example.varswatch.domain.model.SearchResults.Item
import com.example.varswatch.domain.model.VideoData
import com.example.varswatch.domain.util.Resource

interface YoutubeRepository {

    suspend fun createNewUser(email:String):Resource<Boolean>

    suspend fun getYoutubeData(id: String): Resource<VideoData>

    suspend fun getSearchResults(query:String): Resource<SearchResults>

    suspend fun getChannelSearchResults(query:String): Resource<SearchResults>

    suspend fun getChannelVideos(channelId:String):Resource<SearchResults>

    suspend fun addVideoToHistory(item: Item)

    suspend fun subscribeToChannel(item: Item): Resource<Boolean>

    suspend fun unsubscribeToChannel(item: Item): Resource<Boolean>

    suspend fun getSubscribedChannels():Resource<List<Item>>

    suspend fun getHistory():Resource<List<Item>>

    suspend fun getPlayList(playListName:String):Resource<List<Item>>

    suspend fun addVideoToPlayList(item: Item, playListName: String)

    suspend fun addNewPlayList(playListName: String)

    suspend fun getListOfPlayLists():Resource<List<String>>

}