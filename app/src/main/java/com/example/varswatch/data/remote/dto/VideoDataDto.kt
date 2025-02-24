package com.example.varswatch.data.remote.dto

import com.example.varswatch.domain.model.VideoData

data class VideoDataDto(
    var items: List<Item>?=null,
) {

    fun toVideoData(): VideoData {
        return VideoData(
            items = if(items!=null) items!!.map { it.toVideoDataItems() } else emptyList()
        )
    }

    data class Item(
        var id: String?=null,
        var contentDetails: ContentDetails?=null,
        var statistics: Statistics?=null
    ) {

        fun toVideoDataItems(): VideoData.Item {
            this.apply{
                return VideoData.Item(
                    id = id ?: "",
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

        data class ContentDetails(
            var duration: String?=null,
            var dimension: String?=null,
            var definition: String?=null,
            var caption: String?=null,
            var licensedContent: Boolean?=null,
            var projection: String?=null
        )

        data class Statistics(
            var viewCount: String?=null,
            var likeCount: String?=null,
            var favoriteCount: String?=null,
            var commentCount: String?=null
        )
    }
}


