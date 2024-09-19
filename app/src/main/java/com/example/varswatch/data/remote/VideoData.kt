package com.example.varswatch.data.remote

data class VideoData(
    val items: List<Item?>?,
    val pageInfo: PageInfo?
) {
    data class Item(
        val id: String?,
        val snippet: Snippet?,
        val contentDetails: ContentDetails?,
        val statistics: Statistics?
    ) {
        data class Snippet(
            val publishedAt: String?,
            val channelId: String?,
            val title: String?,
            val description: String?,
            val thumbnails: Thumbnails?,
            val channelTitle: String?,
            val tags: List<String?>?,
            val categoryId: String?,
            val liveBroadcastContent: String?,
            val localized: Localized?,
            val defaultAudioLanguage: String?
        ) {
            data class Thumbnails(
                val default: Default?,
                val medium: Medium?,
                val high: High?,
                val standard: Standard?,
                val maxres: Maxres?
            ) {
                data class Default(
                    val url: String?,
                    val width: Int?,
                    val height: Int?
                )

                data class Medium(
                    val url: String?,
                    val width: Int?,
                    val height: Int?
                )

                data class High(
                    val url: String?,
                    val width: Int?,
                    val height: Int?
                )

                data class Standard(
                    val url: String?,
                    val width: Int?,
                    val height: Int?
                )

                data class Maxres(
                    val url: String?,
                    val width: Int?,
                    val height: Int?
                )
            }

            data class Localized(
                val title: String?,
                val description: String?
            )
        }

        data class ContentDetails(
            val duration: String?,
            val dimension: String?,
            val definition: String?,
            val caption: String?,
            val licensedContent: Boolean?,
            val contentRating: ContentRating?,
            val projection: String?
        ) {
            class ContentRating
        }

        data class Statistics(
            val viewCount: String?,
            val likeCount: String?,
            val favoriteCount: String?,
            val commentCount: String?
        )
    }

    data class PageInfo(
        val totalResults: Int?,
        val resultsPerPage: Int?
    )
}


