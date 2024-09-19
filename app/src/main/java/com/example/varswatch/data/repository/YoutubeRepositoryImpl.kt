package com.example.varswatch.data.repository

import com.example.varswatch.data.remote.SearchResultsDto
import com.example.varswatch.data.remote.VideoData
import com.example.varswatch.data.remote.YoutubeApi
import com.example.varswatch.domain.repository.YoutubeRepository
import com.example.varswatch.domain.util.Resource
import javax.inject.Inject

class YoutubeRepositoryImpl @Inject constructor(
    private val Youtubeapi: YoutubeApi
) : YoutubeRepository {

    override suspend fun getYoutubeData(id: String): Resource<VideoData> {
        return try {
            Resource.Success(
                data = Youtubeapi.getVideoData(
                    id
                )
            )
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Error(e.message?:"An error occurred")
        }
    }

    override suspend fun getSearchResults(query: String): Resource<SearchResultsDto> {
        return try {
            Resource.Success(
                data = Youtubeapi.search(query)
            )
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Error(e.message?:"An error occurred")
        }
    }

}