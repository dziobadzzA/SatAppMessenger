package com.satellite.messenger.pager.fragment.info.adapter

import androidx.recyclerview.widget.RecyclerView
import com.satellite.messenger.R
import com.satellite.messenger.databinding.LayoutItemBinding
import com.satellite.messenger.utils.state.Satellite

class InfoViewHolder(
    private val binding: LayoutItemBinding,
    private val listener: InfoListener,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(channel: Satellite?) {
        when(channel?.labelimage) {
            "1" -> binding.imageUser.setImageResource(R.drawable.intelsat)
            "2" -> binding.imageUser.setImageResource(R.drawable.eutelsat)
            else -> binding.imageUser.setImageResource(R.drawable.sat)
        }
        binding.imageUser.setOnClickListener {
            listener.show(channel!!)
        }
        binding.textUser.text = channel?.name + " position: " + channel?.point
    }
}