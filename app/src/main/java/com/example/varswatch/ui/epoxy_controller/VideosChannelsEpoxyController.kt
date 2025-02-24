package com.example.varswatch.ui.epoxy_controller

import com.airbnb.epoxy.TypedEpoxyController
import com.bumptech.glide.Glide
import com.example.varswatch.R
import com.example.varswatch.ViewBindingKotlinModel
import com.example.varswatch.databinding.ChannelItemBinding
import com.example.varswatch.databinding.SearchVideoItemBinding
import com.example.varswatch.domain.model.SearchResults.Item

class VideosChannelsEpoxyController(
    private val itemClickListener: OnItemClickListener
) : TypedEpoxyController<List<Item>>() {

    interface OnItemClickListener {
        fun onItemClick(item: Item,subscribe:Boolean=false,saveToPlayList:Boolean=false)
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
                itemClickListener.onItemClick(item, subscribe = true)
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

            duration.text = convert(item.contentDetails.duration)

            saveButton.setOnClickListener {
                itemClickListener.onItemClick(item,saveToPlayList = true)
            }

            root.setOnClickListener {
                itemClickListener.onItemClick(item)
            }

        }

        private fun convert(tim: String): String {
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
