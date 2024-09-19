package com.example.varswatch.ui.search

import com.airbnb.epoxy.TypedEpoxyController
import com.bumptech.glide.Glide
import com.example.varswatch.R
import com.example.varswatch.ViewBindingKotlinModel
import com.example.varswatch.data.remote.SearchResultsDto
import com.example.varswatch.databinding.SearchVideoItemBinding

class SearchEpoxyController(
    private val itemClickListener: OnItemClickListener
) : TypedEpoxyController<HashMap<String, SearchResultsDto.Item>>() {

    interface OnItemClickListener {
        fun onItemClick(item: SearchResultsDto.Item)
    }

    override fun buildModels(data: HashMap<String, SearchResultsDto.Item>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        data.forEach {
            ItemEpoxyModel(it.value, itemClickListener).id(it.key).addTo(this)
        }
    }

    data class ItemEpoxyModel(
        val item: SearchResultsDto.Item,
        private val itemClickListener: OnItemClickListener,
    ) : ViewBindingKotlinModel<SearchVideoItemBinding>(R.layout.search_video_item) {

        override fun SearchVideoItemBinding.bind() {

            Glide.with(thumnailPicture)
                .load(item.snippet.thumbnails.high.url)
                .into(thumnailPicture)

            thumbnail.text = item.snippet.title
            channelName.text = item.snippet.channelTitle

            if (item.contentDetails != null) {
                duration.text = convert(item.contentDetails!!.duration!!)
            }

            root.setOnClickListener {
                itemClickListener.onItemClick(item)
            }

        }

        fun convert(tim: String): String {
            val n = tim.length
            val ans = StringBuilder()

            for (i in 2 until n) {
                if (tim[i] in '0'..'9') {
                    ans.append(tim[i])
                } else {
                    ans.append(':')
                }
            }

            if (ans.isNotEmpty()) {
                ans.setLength(ans.length - 1) // Remove the last character
            }

            return ans.toString()
        }

    }

}
