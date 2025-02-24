package com.example.varswatch.domain.model

class VideoData(
    var items: List<Item>,
) {
    data class Item(
        var id: String,
        var contentDetails: ContentDetails,
        var statistics: Statistics
    ) {
        data class ContentDetails(
            var duration: String,
            var dimension: String,
            var definition: String,
            var caption: String,
            var licensedContent: Boolean,
            var projection: String
        )

        data class Statistics(
            var viewCount: String,
            var likeCount: String,
            var favoriteCount: String,
            var commentCount: String
        )
    }
}
