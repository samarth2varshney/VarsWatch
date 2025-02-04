package com.example.varswatch.ui.search

import com.airbnb.epoxy.TypedEpoxyController
import com.bumptech.glide.Glide
import com.example.varswatch.R
import com.example.varswatch.ViewBindingKotlinModel
import com.example.varswatch.data.remote.SearchResultsDto.Item
import com.example.varswatch.databinding.ChannelItemBinding
import com.example.varswatch.databinding.SearchVideoItemBinding

class SearchEpoxyController(
    private val itemClickListener: OnItemClickListener
) : TypedEpoxyController<List<Item>>() {

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    override fun buildModels(data: List<Item>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        data.forEach {
            if(it.id.kind=="youtube#channel")
                ChannelEpoxyModel(it, itemClickListener).id(it.id.channelId).addTo(this)
            else
                VideoEpoxyModel(it, itemClickListener).id(it.id.videoId).addTo(this)
        }
    }

    data class ChannelEpoxyModel(val item: Item,
                                 private val itemClickListener: OnItemClickListener,
    ) : ViewBindingKotlinModel<ChannelItemBinding>(R.layout.channel_item) {

        override fun ChannelItemBinding.bind() {

            Glide.with(channelImage)
                .load(item.snippet.thumbnails.high.url)
                .into(channelImage)

            channelName.text = item.snippet.channelTitle

            subscribeButton.setOnClickListener {
                itemClickListener.onItemClick(item)
            }

        }
    }

    data class VideoEpoxyModel(
        val item: Item,
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
