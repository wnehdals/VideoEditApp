package com.jdm.videoeditapp.ui.media.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jdm.videoeditapp.databinding.ItemPickPhotoBinding
import com.jdm.videoeditapp.model.MediaType
import com.jdm.videoeditapp.model.SourceMedia
import com.jdm.videoeditapp.util.TimeUtil


class MediaPickAdapter(
    private val context: Context,
    private val onClickImageView: (SourceMedia, Int) -> Unit = { sourceMedia, position -> }
    //private val onClickSelectNum: (BaseMedia, Int) -> Unit = { selectedItem, position -> },
    //private val showLimitDialog: () -> Unit = {}
) : RecyclerView.Adapter<MediaPickAdapter.ViewHolder>() {
    val mediaList = mutableListOf<SourceMedia>()
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
    fun addData(data: MutableList<SourceMedia>) {
        mediaList.clear()
        mediaList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemPickPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SourceMedia, position: Int) {
            when (item.type) {
                MediaType.VIDEO -> {
                    Glide.with(context).load(item.uri).into(binding.itemGalleryImg)
                    binding.itemPickPhotoDurationTv.text = TimeUtil.intToHourFormat(item.duration)
                }
                MediaType.GIF -> {
                    Glide.with(context).load(item.uri).into(binding.itemGalleryImg)
                }
                MediaType.PHOTO -> {
                    Glide.with(context).load(item.uri).into(binding.itemGalleryImg)
                }
                else -> return
            }
            binding.itemGalleryImg.setOnClickListener {
                onClickImageView(item, position)
            }



        }
    }

}