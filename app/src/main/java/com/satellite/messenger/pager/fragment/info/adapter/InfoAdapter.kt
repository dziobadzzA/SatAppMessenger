package com.satellite.messenger.pager.fragment.info.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.satellite.messenger.databinding.LayoutItemBinding
import com.satellite.messenger.utils.state.Satellite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InfoAdapter(private val listener: InfoListener): ListAdapter<Satellite, InfoViewHolder>(InfoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutItemBinding.inflate(layoutInflater, parent, false)
        return InfoViewHolder(binding, listener)
    }

    override fun onViewAttachedToWindow(holder: InfoViewHolder) {
        super.onViewAttachedToWindow(holder)

        if (holder.layoutPosition == (holder.bindingAdapter?.itemCount ?: 0) - 1) {
            GlobalScope.launch(Dispatchers.Main) {
                // listener.getNewItem()
            }
        }
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.bind(getItem(holder.layoutPosition))
    }



}