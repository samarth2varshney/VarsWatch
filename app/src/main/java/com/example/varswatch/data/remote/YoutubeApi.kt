package com.example.varswatch.data.remote

import com.example.varswatch.domain.util.AppConstants.key
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {

    @GET("videos?key=${key}&part=snippet,contentDetails,statistics,status")
    suspend fun getVideoData(
        @Query("id") videoId: String
    ): VideoData

    @GET("search?part=snippet&q=t series&type=video,channel&maxResults=10&key=${key}")
    suspend fun search(
        @Query("q") query:String
    ) : SearchResultsDto

}