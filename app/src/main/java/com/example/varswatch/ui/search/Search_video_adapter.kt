package com.example.varswatch.ui.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.varswatch.CustomUiActivity
import com.example.varswatch.R
import com.example.varswatch.data.SearchResultsDto

class search_video_adapter(private val context: Context, private val arr: List<SearchResultsDto.Item>,
) : RecyclerView.Adapter<search_video_adapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.imageView3)
        val title = itemView.findViewById<TextView>(R.id.thumbnail)
        val channelName = itemView.findViewById<TextView>(R.id.channel_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_video_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val video = arr[position]

        Glide.with(context)
            .load(video.snippet.thumbnails.high.url)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val intent2 = Intent(context, CustomUiActivity::class.java)
            intent2.putExtra("youtubelink",video.id.videoId)
            intent2.putExtra("youtubetitle",video.snippet.title)
            context.startActivity(intent2)
        }

        holder.title.text = video.snippet.title
        holder.channelName.text = video.snippet.channelTitle

    }

    override fun getItemCount(): Int {
        return arr.size
    }
}