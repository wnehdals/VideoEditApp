package com.jdm.videoeditapp.ui.media.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jdm.videoeditapp.databinding.ItemPickPhotoBinding
import com.jdm.videoeditapp.model.BaseMedia
import com.jdm.videoeditapp.model.Photo
import com.jdm.videoeditapp.model.Video
import dagger.hilt.android.AndroidEntryPoint


class MediaPickAdapter(
    private val context: Context
    //private val onClickImageView: (BaseMedia, Int) -> Unit = { selectedItem, position -> },
    //private val onClickSelectNum: (BaseMedia, Int) -> Unit = { selectedItem, position -> },
    //private val showLimitDialog: () -> Unit = {}
) : RecyclerView.Adapter<MediaPickAdapter.ViewHolder>() {
    val mediaList = mutableListOf<BaseMedia>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPickPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mediaList[position]
        if (item != null) {
            holder.bind(item, position)
        }
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }
    fun addData(data: MutableList<Video>) {
        mediaList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemPickPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BaseMedia, position: Int) {
            if (item is Video) {
                Log.e("jdm_tag", "video")
            } else if (item is Photo) {
                Log.e("jdm_tag", "photo")
            }
            Glide.with(context).load(item.uri).into(binding.itemGalleryImg)


        }
    }

}