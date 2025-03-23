package com.example.varswatch.data.remote

import com.example.varswatch.data.remote.dto.SearchResultsDto
import com.example.varswatch.data.remote.dto.VideoDataDto
import com.example.varswatch.domain.util.AppConstants.key
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {

    @GET("videos?part=snippet,contentDetails,statistics,status")
    suspend fun getVideoData(
        @Query("id") videoId: String,
        @Query("key") key:String
    ): VideoDataDto

    @GET("search?part=snippet&type=channel,video&maxResults=10")
    suspend fun search(
        @Query("q") query:String,
        @Query("key") key:String
    ) : SearchResultsDto

    @GET("search?part=snippet&type=channel&maxResults=1")
    suspend fun searchChannels(
        @Query("q") query:String,
        @Query("key") key:String
    ) : SearchResultsDto

    @GET("search?part=snippet&type=video&order=relevance&maxResults=5")
    suspend fun getChannelVideos(
        @Query("channelId") channelId:String,
        @Query("key") key:String
    ) : SearchResultsDto

}