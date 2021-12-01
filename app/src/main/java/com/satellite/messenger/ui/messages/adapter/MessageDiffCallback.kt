package com.satellite.messenger.ui.messages.adapter

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.satellite.messenger.utils.state.ServerMessage

class MessageDiffCallback:DiffUtil.ItemCallback<ServerMessage>() {

    override fun areItemsTheSame(oldItem: ServerMessage, newItem: ServerMessage): Boolean {
        return oldItem.datetime == newItem.datetime
    }

    override fun areContentsTheSame(oldItem: ServerMessage, newItem: ServerMessage): Boolean {
        return oldItem == newItem
    }

    @Nullable
    @Override
    override fun getChangePayload(oldItem: ServerMessage, newItem: ServerMessage): Any? {
        return super.getChangePayload(oldItem, newItem)
    }
}