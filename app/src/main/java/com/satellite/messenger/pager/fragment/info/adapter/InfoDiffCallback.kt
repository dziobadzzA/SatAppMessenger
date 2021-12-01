package com.satellite.messenger.pager.fragment.info.adapter

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.satellite.messenger.utils.state.Satellite

class InfoDiffCallback:DiffUtil.ItemCallback<Satellite>() {

    override fun areItemsTheSame(oldItem: Satellite, newItem: Satellite): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Satellite, newItem: Satellite): Boolean {
        return oldItem == newItem
    }

    @Nullable
    @Override
    override fun getChangePayload(oldItem: Satellite, newItem: Satellite): Any? {
        return super.getChangePayload(oldItem, newItem)
    }
}