package com.jdm.videoeditapp.ui.media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdm.videoeditapp.R
import com.jdm.videoeditapp.base.BaseFragment
import com.jdm.videoeditapp.databinding.FragmentGifPhotoSourceBinding

class GifPhotoSourceFragment : BaseFragment<FragmentGifPhotoSourceBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_gif_photo_source

    override fun initView() {
    }

    override fun subscribe() {
    }

    override fun initEvent() {
    }

    companion object {
        @JvmStatic
        fun newInstance() = GifPhotoSourceFragment()
        const val TAG = "GifPhotoSourceFragment"
    }
}