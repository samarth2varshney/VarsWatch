package com.example.varswatch.data.remote

import com.example.varswatch.data.remote.dto.SearchResultsDto
import com.example.varswatch.data.remote.dto.VideoDataDto
import com.example.varswatch.domain.util.AppConstants.key
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {

    @GET("videos?key=${key}&part=snippet,contentDetails,statistics,status")
    suspend fun getVideoData(
        @Query("id") videoId: String
    ): VideoDataDto

    @GET("search?part=snippet&type=channel,video&maxResults=10&key=${key}")
    suspend fun search(
        @Query("q") query:String
    ) : SearchResultsDto

    @GET("search?part=snippet&type=channel&maxResults=1&key=${key}")
    suspend fun searchChannels(
        @Query("q") query:String
    ) : SearchResultsDto

    @GET("search?part=snippet&type=video&order=relevance&maxResults=5&key=${key}")
    suspend fun getChannelVideos(
        @Query("channelId") channelId:String
    ) : SearchResultsDto

}