package com.satellite.messenger.ui.messages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.satellite.messenger.databinding.ListMessagesBinding
import com.satellite.messenger.utils.state.ServerMessage

class MessageAdapter: ListAdapter<ServerMessage, MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListMessagesBinding.inflate(layoutInflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(holder.layoutPosition))
    }
}