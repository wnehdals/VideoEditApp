package com.jdm.videoeditapp.ui.media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdm.videoeditapp.R
import com.jdm.videoeditapp.base.BaseFragment
import com.jdm.videoeditapp.databinding.FragmentPhotoSourceBinding

class PhotoSourceFragment : BaseFragment<FragmentPhotoSourceBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_photo_source

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun subscribe() {
        TODO("Not yet implemented")
    }

    override fun initEvent() {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun newInstance() = PhotoSourceFragment()
        const val TAG = "PhotoSourceFragment"
    }
}