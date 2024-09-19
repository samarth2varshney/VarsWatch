package com.example.varswatch.data.remote

import com.example.varswatch.domain.util.AppConstants.key
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {

    @GET("videos?key=${key}&part=snippet,contentDetails,statistics,status")
    suspend fun getVideoData(
        @Query("id") videoId: String
    ): VideoData

    @GET("search?key=${key}&type=any&part=snippet&maxResults=10")
    suspend fun search(
        @Query("q") query:String
    ) : SearchResultsDto

}