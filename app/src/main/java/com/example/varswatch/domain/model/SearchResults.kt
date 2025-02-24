package com.example.varswatch.domain.model

import com.example.varswatch.domain.model.VideoData.Item.ContentDetails
import com.example.varswatch.domain.model.VideoData.Item.Statistics

data class SearchResults(
    var nextPageToken: String,
    var pageInfo: PageInfo,
    var items: List<Item>
) {
    data class PageInfo(
        var totalResults: Int,
        var resultsPerPage: Int
    )

    data class Item(
        var id: Id,
        var snippet: Snippet,
        var contentDetails: ContentDetails,
        var statistics: Statistics
    ) {
        data class Id(
            var kind: String,
            var videoId: String,
            var channelId: String
        )

        data class Snippet(
            var publishedAt: String,
            var channelId: String,
            var title: String,
            var description: String,
            var thumbnails: Thumbnails,
            var channelTitle: String,
            var liveBroadcastContent: String
        ) {
            data class Thumbnails(
                var high: High
            ) {
                data class High(
                    var url: String
                )
            }
        }
    }
}
