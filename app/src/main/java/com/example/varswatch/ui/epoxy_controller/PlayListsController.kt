package com.example.varswatch.ui.epoxy_controller

import com.airbnb.epoxy.TypedEpoxyController
import com.example.varswatch.R
import com.example.varswatch.ViewBindingKotlinModel
import com.example.varswatch.databinding.PlayListItemBinding

class PlayListsController(
    private val itemClickListener: OnItemClickListener
) : TypedEpoxyController<List<String>>() {

    interface OnItemClickListener {
        fun onItemClick(item: String)
    }

    override fun buildModels(data: List<String>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        data.forEach {
            PlayListsEpoxyModel(it, itemClickListener).id(1).addTo(this)
        }
    }

    data class PlayListsEpoxyModel(
        val item: String,
        private val itemClickListener: OnItemClickListener,
    ) : ViewBindingKotlinModel<PlayListItemBinding>(R.layout.play_list_item) {

        override fun PlayListItemBinding.bind() {

            playListName.text = item

            root.setOnClickListener {
                itemClickListener.onItemClick(item)
            }

        }

    }

}