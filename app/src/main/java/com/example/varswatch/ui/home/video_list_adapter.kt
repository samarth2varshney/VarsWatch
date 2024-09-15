package com.example.drive.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.varswatch.VideoPlayerActivity
import com.example.varswatch.R

class video_list_adapter(private val context: Context, private val map: MutableMap<String, Any>?,
) : RecyclerView.Adapter<video_list_adapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.recyclerView)
        val title = itemView.findViewById<TextView>(R.id.textView3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val arr = map!!["arr${position+1}"] as ArrayList<String>

        holder.title.text = arr[0]

        holder.recyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL,true)
        val adapter = video_adapter(context, arr)
        holder.recyclerView.adapter = adapter

    }

    override fun getItemCount(): Int {
        return map!!.size
    }
}

class video_adapter(private val context: Context, private val arr: ArrayList<String>,
) : RecyclerView.Adapter<video_adapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.thumnailPicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load("https://img.youtube.com/vi/${arr[position + 1]}/hqdefault.jpg")
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val intent2 = Intent(context, VideoPlayerActivity::class.java)
            intent2.putExtra("youtubelink",arr[position + 1])
            context.startActivity(intent2)
        }

    }

    override fun getItemCount(): Int {
        return arr.size - 1
    }
}