package com.satellite.messenger.pager.fragment.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.satellite.messenger.databinding.FragmentImageBinding
import com.satellite.messenger.di.DaggerAppComponent
import com.satellite.messenger.pager.fragment.DataItem

class ImagePager(private val dataItem: DataItem): Fragment() {

    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var imageViewModel: ImageViewModel

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelFactory = DaggerAppComponent.create().viewFactory()
        imageViewModel = ViewModelProvider(this, viewModelFactory)[ImageViewModel::class.java]
        imageViewModel.initList(dataItem.getItem().coverage)

        _binding = FragmentImageBinding.inflate(inflater, container, false)

        binding.imageCoverage.setOnClickListener {
            imageViewModel.position++

            if (imageViewModel.position == imageViewModel.list.size)
                imageViewModel.position = 0

            loadImage(imageViewModel.list[imageViewModel.position])
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (imageViewModel.list.size > 0)
            loadImage(imageViewModel.list[imageViewModel.position])
    }

    private fun loadImage(str:String) {
        Glide.with(binding.root.context).load(str).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imageCoverage)
    }

}