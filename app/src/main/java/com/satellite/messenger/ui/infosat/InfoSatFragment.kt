package com.satellite.messenger.ui.infosat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.satellite.messenger.databinding.FragmentSatelliteBinding
import com.satellite.messenger.di.DaggerAppComponent
import com.satellite.messenger.pager.ViewPagerAdapter
import com.satellite.messenger.pager.ViewPagerTransformer
import com.satellite.messenger.pager.fragment.DataItem
import com.satellite.messenger.utils.TAB_TITLES
import com.satellite.messenger.utils.state.Satellite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InfoSatFragment: Fragment(), DataItem {

    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var infoSatViewModel: InfoSatViewModel
    private var _binding: FragmentSatelliteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

       viewModelFactory = DaggerAppComponent.create().viewFactory()

        infoSatViewModel = ViewModelProvider(this, viewModelFactory)[InfoSatViewModel::class.java]

        if (infoSatViewModel.items.value.isNullOrEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                infoSatViewModel.getItem()
            }
        }
        _binding = FragmentSatelliteBinding.inflate(inflater, container, false)

        binding.viewpager.adapter = ViewPagerAdapter(this, this)
        binding.viewpager.setPageTransformer(ViewPagerTransformer())
        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            tab.text = getString(TAB_TITLES[position])
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setItem(channel: Satellite) {
        infoSatViewModel.item = channel
    }

    override fun deleteItem(position:Int) {
        infoSatViewModel.items.value?.removeAt(position)
    }

    override fun getItem(): Satellite = infoSatViewModel.item

    override fun getItems(): List<Satellite> = infoSatViewModel.items.value?.toList()!!

}