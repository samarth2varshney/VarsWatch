package com.example.varswatch.ui.epoxy_controller

import com.airbnb.epoxy.TypedEpoxyController
import com.bumptech.glide.Glide
import com.example.varswatch.R
import com.example.varswatch.ViewBindingKotlinModel
import com.example.varswatch.databinding.ChannelItemBinding
import com.example.varswatch.domain.model.SearchResults.Item

class ChannelsEpoxyController(
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
            ChannelEpoxyModel(it, itemClickListener).id(it.id.channelId).addTo(this)
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

}
