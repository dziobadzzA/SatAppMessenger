package com.satellite.messenger.pager.fragment.text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.satellite.messenger.R
import com.satellite.messenger.databinding.FragmentTextBinding
import com.satellite.messenger.di.DaggerAppComponent
import com.satellite.messenger.pager.fragment.DataItem

class TextPager(private val dataItem: DataItem, private val type:String):Fragment() {

    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var textViewModel: TextViewModel

    private var _binding: FragmentTextBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelFactory = DaggerAppComponent.create().viewFactory()
        textViewModel = ViewModelProvider(this, viewModelFactory)[TextViewModel::class.java]

        _binding = FragmentTextBinding.inflate(inflater, container, false)

        when(type) {
            "TV" -> drawableTable(R.array.typeTV, dataItem.getItem().TV)
            "beacon" -> drawableTable(R.array.typeBeacon, dataItem.getItem().beacon)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun drawableTable(int:Int, str:String) {
        activity?.let { tableView(binding.tableLayout, it.applicationContext, str.split(";").toList(), resources.getStringArray(
            int).toList()) }
    }

}