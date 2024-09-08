package com.example.varswatch.domain.repository

import com.example.varswatch.data.SearchResultsDto
import com.example.varswatch.data.remote.VideoData
import com.example.varswatch.domain.util.Resource

interface YoutubeRepository {

    suspend fun getYoutubeData(id: String): Resource<VideoData>

    suspend fun getSearchResults(query:String): Resource<SearchResultsDto>

}