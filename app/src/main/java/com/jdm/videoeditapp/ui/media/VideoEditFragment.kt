package com.jdm.videoeditapp.ui.media

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.jdm.videoeditapp.R
import com.jdm.videoeditapp.base.BaseFragment
import com.jdm.videoeditapp.databinding.FragmentVideoEditBinding
import com.jdm.videoeditapp.ui.event.PlayState
import com.jdm.videoeditapp.ui.event.UIEvent
import com.jdm.videoeditapp.ui.media.adapter.TimeLineAdapter
import com.jdm.videoeditapp.util.PlayBackStateListener
import com.jdm.videoeditapp.util.TimeUtil.getTimeFormatString
import com.jdm.videoeditapp.util.VariableScrollSpeedLinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class VideoEditFragment : BaseFragment<FragmentVideoEditBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_video_edit
    private val sourceViewModel: SourceViewModel by hiltNavGraphViewModels(R.id.source)
    private val playViewModel: PlayViewModel by viewModels()
    private val timelineAdapter by lazy { TimeLineAdapter(requireContext(), sourceViewModel) }
    private var player: ExoPlayer? = null
    private var overallXScroll = 0
    private lateinit var playBackStateListener: PlayBackStateListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sourceViewModel.clearProject()
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun initView() {
        buildPlayer()
        playBackStateListener = PlayBackStateListener(playViewModel)
        player?.addListener(playBackStateListener)
        player?.let { binding.videoEditControlsView.player = it }
        sourceViewModel.setThumbnailList(requireContext())
        binding.videoEditTimeLineTv.text = getTimeFormatString(0)
        var linearLayoutManager = VariableScrollSpeedLinearLayoutManager(requireContext(), 1f)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        with(binding) {
            videoEditTimeLineRv.adapter = timelineAdapter
            videoEditTimeLineRv.layoutManager = linearLayoutManager
            videoEditTimeLineRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    //sourceViewModel.emitUIEvent(UIEvent.StopVideo)
                    overallXScroll = overallXScroll + dx;
                    binding.videoEditTimeLineTv.text = getTimeFormatString(overallXScroll / sourceViewModel.cropWidth)
                    binding.videoEditPreviewIv.setImageBitmap(timelineAdapter.getItem(overallXScroll / sourceViewModel.cropWidth))
                }
            })
            videoEditControlsView.setProgressUpdateListener { position, bufferedPosition ->
                Log.e("jdm_tag", "${position}")
                var idx = (position / 1000).toInt()
                videoEditTimeLineRv.smoothScrollToPosition(sourceViewModel.divideFrameCnt + idx)
                /*
                lifecycleScope.launch {
                    var sp = (sourceViewModel.divideFrameCnt * position / 1000).toInt()
                    videoEditTimeLineRv.scrollToPosition()

                }

                 */
            }
        }

    }

    override fun subscribe() {
        with(sourceViewModel) {
            thumbnailData.observe(viewLifecycleOwner) {
                timelineAdapter.addData(it)
            }
            loading.observe(viewLifecycleOwner) {
                if (it != null) {
                    if (it) {
                        showProgressDialog()
                    } else {
                        dismissProgressDialog()
                    }
                }
            }

        }
        with(playViewModel) {
            playState.observe(viewLifecycleOwner) {
                when (it) {
                    is PlayState.Loading -> {
                        Log.e("jdm_tag", "player - loading")
                    }
                    is PlayState.Pause -> {
                        Log.e("jdm_tag", "player - pause")
                    }
                    is PlayState.Playing -> {
                        Log.e("jdm_tag", "player - playing")
                    }
                    is PlayState.Ready -> {
                        player?.play()
                        Log.e("jdm_tag", "player - ready")
                    }
                    is PlayState.Finish -> {
                        Log.e("jdm_tag", "player - finish")
                    }
                    else -> return@observe
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            sourceViewModel.uiEvent.collectLatest {
                when (it) {
                    UIEvent.GoToVideoEditFragment -> {
                        findNavController().navigate(AllSourceFragmentDirections.actionAllSourceFragmentToVideoEditFragment())
                    }
                    UIEvent.PlayVideo -> {
                        changePreview(true)
                        readyToPlay()
                    }
                    UIEvent.StopVideo -> {
                        changePreview(false)
                        stopToPlay()
                    }
                }
            }
        }

    }

    override fun initEvent() {
        with(binding) {
            videoEditPlayIv.setOnClickListener {
                sourceViewModel.emitUIEvent(UIEvent.PlayVideo)
            }
        }
    }

    private fun buildPlayer() {
        player = ExoPlayer.Builder(requireContext())
            .build()
        binding.videoEditPlayerView.player = player
    }

    private fun readyToPlay() {
        if (sourceViewModel.project == null)
            return
        if (sourceViewModel.project!!.mediaItem == null)
            return
        player?.setMediaItem(sourceViewModel.project!!.mediaItem!!)
        player?.prepare()
    }
    private fun stopToPlay() {
        player?.let { it.pause() }
    }
    private fun changePreview(isPlaying: Boolean) {
        if (isPlaying) {
            binding.videoEditPreviewIv.visibility = View.GONE
            binding.videoEditPlayerView.visibility = View.VISIBLE
        } else {
            binding.videoEditPreviewIv.visibility = View.VISIBLE
            binding.videoEditPlayerView.visibility = View.INVISIBLE
        }
    }
    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()

    }

    companion object {
        @JvmStatic
        fun newInstance() = VideoEditFragment()
        const val TAG = "VideoEditFragment"
    }
}
