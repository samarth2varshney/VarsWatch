package com.example.varswatch.data.remote.dto

import com.example.varswatch.data.remote.dto.VideoDataDto.Item.ContentDetails
import com.example.varswatch.data.remote.dto.VideoDataDto.Item.Statistics
import com.example.varswatch.domain.model.SearchResults
import com.example.varswatch.domain.model.VideoData

data class SearchResultsDto(
    var nextPageToken: String? = null,
    var pageInfo: PageInfo? = null,
    var items: List<Item>? = null
) {

    fun toSearchResults(): SearchResults {
        return SearchResults(
            nextPageToken = this.nextPageToken ?: "",
            pageInfo = SearchResults.PageInfo(
                totalResults = this.pageInfo?.totalResults ?: 0,
                resultsPerPage = this.pageInfo?.resultsPerPage ?: 0
            ),
            items = if(items!=null) items!!.map { it.toSearchResultsItems() } else emptyList()
        )
    }

    data class PageInfo(
        var totalResults: Int? = null,
        var resultsPerPage: Int? = null
    )

    data class Item(
        var id: Id?=null,
        var snippet: Snippet?=null,
        var contentDetails: ContentDetails?=null,
        var statistics: Statistics?=null
    ) {

        fun toSearchResultsItems(): SearchResults.Item {
            this.apply{
                return SearchResults.Item(
                    id = SearchResults.Item.Id(
                        kind = id?.kind ?: "", // Default empty string if null
                        videoId = id?.videoId ?: "",
                        channelId = id?.channelId ?: ""
                    ),
                    snippet = SearchResults.Item.Snippet(
                        publishedAt = snippet?.publishedAt ?: "",
                        channelId = snippet?.channelId ?: "",
                        title = snippet?.title ?: "",
                        description = snippet?.description ?: "",
                        thumbnails = SearchResults.Item.Snippet.Thumbnails(
                            high = SearchResults.Item.Snippet.Thumbnails.High(
                                url = snippet?.thumbnails?.high?.url ?: ""
                            )
                        ),
                        channelTitle = snippet?.channelTitle ?: "",
                        liveBroadcastContent = snippet?.liveBroadcastContent ?: ""
                    ),
                    contentDetails = VideoData.Item.ContentDetails(
                        duration = contentDetails?.duration ?: "",
                        dimension = contentDetails?.dimension ?: "",
                        definition = contentDetails?.definition ?: "",
                        caption = contentDetails?.caption ?: "",
                        licensedContent = contentDetails?.licensedContent ?: false,
                        projection = contentDetails?.projection ?: ""
                    ),
                    statistics = VideoData.Item.Statistics(
                        viewCount = statistics?.viewCount ?: "0",
                        likeCount = statistics?.likeCount ?: "0",
                        favoriteCount = statistics?.favoriteCount ?: "0",
                        commentCount = statistics?.commentCount ?: "0"
                    )
                )
            }
        }

        data class Id(
            var kind: String?=null,
            var videoId: String?=null,
            var channelId: String?=null
        )

        data class Snippet(
            var publishedAt: String?=null,
            var channelId: String?=null,
            var title: String?=null,
            var description: String?=null,
            var thumbnails: Thumbnails?=null,
            var channelTitle: String?=null,
            var liveBroadcastContent: String?=null
        ) {
            data class Thumbnails(
                var high: High?=null
            ) {
                data class High(
                    var url: String?=null
                )
            }
        }
    }
}