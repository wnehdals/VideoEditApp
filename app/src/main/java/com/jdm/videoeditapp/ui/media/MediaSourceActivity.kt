package com.jdm.videoeditapp.ui.media

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.jdm.videoeditapp.R
import com.jdm.videoeditapp.base.BaseActivity
import com.jdm.videoeditapp.databinding.ActivityMedaiSourceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaSourceActivity : BaseActivity<ActivityMedaiSourceBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_medai_source

    override fun subscribe() {
    }

    override fun initEvent() {
        with(binding) {
        }
    }


    override fun initView() {

    }
}