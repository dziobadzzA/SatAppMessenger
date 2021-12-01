package com.satellite.messenger.pager.fragment.info

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.satellite.messenger.databinding.FragmentInfoBinding
import com.satellite.messenger.di.DaggerAppComponent
import com.satellite.messenger.pager.fragment.DataItem
import com.satellite.messenger.pager.fragment.info.adapter.InfoAdapter
import com.satellite.messenger.pager.fragment.info.adapter.InfoListener
import com.satellite.messenger.pager.fragment.info.adapter.SwipeInfo
import com.satellite.messenger.utils.state.Satellite


class InfoPager(private val dataItem: DataItem):Fragment(), InfoListener {

    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var infoViewModel: InfoPagerViewModel

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    private var infoAdapter: InfoAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModelFactory = DaggerAppComponent.create().viewFactory()
        infoViewModel = ViewModelProvider(this, viewModelFactory)[InfoPagerViewModel::class.java]
        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        val swipeToDeleteCallBack = object : SwipeInfo() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                super.onSwiped(viewHolder, direction)
                val position = viewHolder.layoutPosition
                dataItem.deleteItem(position)
                install()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        infoAdapter = InfoAdapter(this)
        binding.recyclerView.adapter = infoAdapter

        showList()

        binding.selectItem.setOnClickListener {
            install()
            state()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun show(channel: Satellite) {
        dataItem.setItem(channel)
        showList()
    }

    private fun install() {
        infoAdapter!!.submitList(dataItem.getItems())
        showList()
    }

    @SuppressLint("SetTextI18n")
    private fun showList () {
        binding.selectItem.text = dataItem.getItem().name + "..(show list)"
    }

    private fun state() {
        infoViewModel.state = !infoViewModel.state
        binding.recyclerView.isVisible = infoViewModel.state
        binding.recyclerView.isClickable = infoViewModel.state
    }

}