package com.example.drive

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class search_video_adapter(private val context: Context, private val arr: List<Item>,
) : RecyclerView.Adapter<search_video_adapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.imageView3)
        val title = itemView.findViewById<TextView>(R.id.textView4)
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
            Log.i("samarth",video.id.videoId)
            val intent2 = Intent(context, CustomUiActivity::class.java)
            intent2.putExtra("youtubelink",video.id.videoId)
            intent2.putExtra("youtubetitle",video.snippet.title)
            context.startActivity(intent2)
        }

        holder.title.text = video.snippet.title

    }

    override fun getItemCount(): Int {
        return arr.size
    }
}