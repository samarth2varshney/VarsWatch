package com.example.varswatch.data.remote

import com.example.varswatch.data.remote.VideoData.Item.ContentDetails
import com.example.varswatch.data.remote.VideoData.Item.Statistics

data class SearchResultsDto(
    val nextPageToken: String?,
    val pageInfo: PageInfo?,
    val items: List<Item>
) {
    data class PageInfo(
        val totalResults: Int?,
        val resultsPerPage: Int?
    )

    data class Item(
        val id: Id,
        val snippet: Snippet,
        var contentDetails: ContentDetails?,
        var statistics: Statistics?
    ) {
        data class Id(
            val kind: String?,
            val videoId: String?,
            val channelId: String?
        )

        data class Snippet(
            val publishedAt: String?,
            val channelId: String?,
            val title: String?,
            val description: String?,
            val thumbnails: Thumbnails,
            val channelTitle: String?,
            val liveBroadcastContent: String?
        ) {
            data class Thumbnails(
                val high: High
            ) {
                data class High(
                    val url: String?
                )
            }
        }
    }
}