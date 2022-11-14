package com.jdm.videoeditapp.ui.media

import android.os.Bundle
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.jdm.videoeditapp.R
import com.jdm.videoeditapp.base.BaseFragment
import com.jdm.videoeditapp.databinding.FragmentAllSourceBinding
import com.jdm.videoeditapp.model.MediaType
import com.jdm.videoeditapp.model.SourceMedia
import com.jdm.videoeditapp.ui.event.UIEvent
import com.jdm.videoeditapp.ui.media.adapter.MediaPickAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AllSourceFragment : BaseFragment<FragmentAllSourceBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_all_source
    private val viewModel: SourceViewModel by hiltNavGraphViewModels(R.id.source)
    private val mediaPickAdapter by lazy {
        MediaPickAdapter(
            requireContext(),
            this::onClickImageView
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getVideoList()
    }

    override fun initView() {
        with(binding) {
            mediaSourceRv.adapter = mediaPickAdapter
            mediaSourceTl.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        when (tab.position) {
                            0 -> {
                                mediaPickAdapter.addData(viewModel.totalSourceList)
                            }
                            1 -> {
                                mediaPickAdapter.addData(viewModel.videoSourceList)
                            }
                            2 -> {
                                mediaPickAdapter.addData(viewModel.photoSourceList)
                            }
                            3 -> {
                                mediaPickAdapter.addData(viewModel.gifSourceList)
                            }
                            else -> {
                                return
                            }
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })


            //var t = mediaSourceTl.getTabAt(0)
            //t?.select()

        }

    }

    override fun subscribe() {
        with(viewModel) {
            sourceData.observe(viewLifecycleOwner) {
                mediaPickAdapter.addData(it)
            }

        }
        lifecycleScope.launchWhenCreated {
            viewModel.uiEvent.collectLatest {
                when (it) {
                    UIEvent.GoToVideoEditFragment -> {
                        findNavController().navigate(AllSourceFragmentDirections.actionAllSourceFragmentToVideoEditFragment())
                    }
                    else -> return@collectLatest
                }
            }
        }
    }

    override fun initEvent() {
    }

    private fun onClickImageView(sourceMedia: SourceMedia, position: Int) {
        if (sourceMedia.type == MediaType.VIDEO) {
            viewModel.setProject(sourceMedia)
            viewModel.emitUIEvent(UIEvent.GoToVideoEditFragment)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = AllSourceFragment()
        const val TAG = "AllSourceFragment"
    }
}