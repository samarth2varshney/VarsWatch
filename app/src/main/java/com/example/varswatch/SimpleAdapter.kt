package com.example.varswatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SimpleAdapter(private val callback: ((String) -> Unit)? = null, private val layoutResId: Int)
    : RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

    private val items = DataSource.items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false))


    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].also { item ->
            holder.bind(item)
            holder.itemView.setOnClickListener {
                callback?.invoke(item.previewUrl)
            }
            Glide.with(holder.itemView)
                .load(item.previewUrl)
                .into(holder.itemView.findViewById<ImageView>(R.id.imagePreview))
            holder.itemView.findViewById<TextView>(R.id.textTitle)
            holder.itemView.findViewById<TextView>(R.id.textTitle).text = item.name
            holder.itemView.findViewById<TextView>(R.id.textSubtitle).text = item.subname
            holder.itemView.findViewById<ImageView>(R.id.imageLogo)?.setImageResource(item.thumbID)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Item) {

        }
    }
}

object DataSource {
    val items by lazy {
        mutableListOf<Item>().apply {
            repeat(5) {
                add(Item("The Secret Life of Walter Mitty", "Beta",
                    "https://image-ticketfly.imgix.net/00/01/61/65/25-og.jpg?w=500&h=334&fit=crop&crop=top",
                    R.drawable.applogo))

                add(Item("Breaking Bad 1.3", "Netflix",
                    "https://ichef.bbci.co.uk/news/660/cpsprodpb/1094D/production/_108471976_95214ef6-d0b9-462c-98e9-9d0a86f2d135.jpg",
                    R.drawable.applogo))

                add(Item("Pulp Fiction - Go into The Story", "Kinopoisk",
                    "https://miro.medium.com/max/2400/1*9QwZkXU_gfk5FJotW-dCnA.jpeg",
                    R.drawable.applogo))

                add(Item("If Joaquin Phoenix Is Willing to Do It?!", "IndieWire",
                    "https://www.indiewire.com/wp-content/uploads/2019/06/joker-movie-fb.jpg?w=768",
                    R.drawable.applogo))
            }
        }
    }
}

data class Item(
    val name: String,
    val subname: String,
    val previewUrl: String,
    val thumbID: Int
)