package com.satellite.messenger.pager.fragment.video

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.satellite.messenger.databinding.FragmentVideoBinding
import com.satellite.messenger.di.DaggerAppComponent
import com.satellite.messenger.pager.fragment.DataItem

class VideoPager(private val dataItem: DataItem): Fragment() {

    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var videoViewModel: VideoViewModel

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaPlayer: MediaController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelFactory = DaggerAppComponent.create().viewFactory()
        videoViewModel = ViewModelProvider(this, viewModelFactory)[VideoViewModel::class.java]
        _binding = FragmentVideoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        videoViewModel.saveState(binding.videoView.currentPosition, binding.videoView.isPlaying)
        _binding?.videoView?.pause()
    }

    private fun initState() {

        mediaPlayer = MediaController(activity)

        _binding?.videoView?.requestFocus(0)
        _binding?.videoView?.setMediaController(mediaPlayer)

        if (videoViewModel.videoUrl.isEmpty() || (videoViewModel.videoUrl != dataItem.getItem().video)) {
            videoViewModel.videoUrl = dataItem.getItem().video
            videoViewModel.saveState(0, binding.videoView.isPlaying)
        }

        _binding?.videoView?.setVideoURI(Uri.parse(videoViewModel.videoUrl))
        mediaPlayer.setAnchorView(_binding?.videoView)

        if (videoViewModel.position.value!! > 0)
            _binding?.videoView?.seekTo(videoViewModel.position.value!!)

        if (videoViewModel.play)
            _binding?.videoView?.start()

    }
}