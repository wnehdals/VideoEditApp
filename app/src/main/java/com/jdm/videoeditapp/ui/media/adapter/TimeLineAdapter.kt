package com.jdm.videoeditapp.ui.media.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdm.videoeditapp.databinding.ItemTimelineBinding
import com.jdm.videoeditapp.ui.media.SourceViewModel

class TimeLineAdapter(
    private var context: Context,
    private var viewModel: SourceViewModel
) : RecyclerView.Adapter<TimeLineAdapter.ViewHolder>() {
    private var thumbnailList: MutableList<Bitmap> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTimelineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = thumbnailList[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return thumbnailList.size
    }

    fun addData(list: MutableList<Bitmap>) {
        thumbnailList.addAll(list)
        notifyDataSetChanged()
    }
    fun getItem(pos: Int): Bitmap {
        val idx = pos + (viewModel.divideFrameCnt/2)
        return thumbnailList[idx]
    }

    inner class ViewHolder(val binding: ItemTimelineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Bitmap, pos: Int) {
            if (pos - (viewModel.divideFrameCnt/2) > 0 && (pos - (viewModel.divideFrameCnt/2)) % 5 == 0 && pos <= thumbnailList.size - (viewModel.divideFrameCnt/2)) {
                binding.itemTimelineCircleV.visibility = View.VISIBLE
            } else {
                binding.itemTimelineCircleV.visibility = View.INVISIBLE
            }
            binding.iteTimelineIv.setImageBitmap(item)
        }
    }
}