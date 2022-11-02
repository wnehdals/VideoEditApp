package com.jdm.videoeditapp.ui.media

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.jdm.videoeditapp.R
import com.jdm.videoeditapp.base.BaseFragment
import com.jdm.videoeditapp.databinding.FragmentAllSourceBinding
import com.jdm.videoeditapp.ui.media.adapter.MediaPickAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllSourceFragment : BaseFragment<FragmentAllSourceBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_all_source
    private val viewModel: SourceViewModel by hiltNavGraphViewModels(R.id.source)
    private val mediaPickAdapter by lazy {
        MediaPickAdapter(requireContext())
    }
    override fun initView() {
        with(binding) {
            mediaSourceRv.adapter = mediaPickAdapter

            mediaSourceTl.addOnTabSelectedListener(object : OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        when(tab.position) {
                            0 -> {
                                viewModel.getVideoList()
                            }
                            1 -> {

                            }
                            2 -> {

                            }
                            else -> {

                            }
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
            var t = mediaSourceTl.getTabAt(0)
            t?.select()
            viewModel.getVideoList()
        }

    }

    override fun subscribe() {
        with(viewModel) {
            videoData.observe(viewLifecycleOwner) {
                mediaPickAdapter.addData(it)
            }
        }
    }

    override fun initEvent() {
    }

    companion object {
        @JvmStatic
        fun newInstance() = AllSourceFragment()
        const val TAG = "AllSourceFragment"
    }
}