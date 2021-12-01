package com.satellite.messenger.ui.phone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.satellite.messenger.databinding.LayoutItemPhoneBinding
import com.satellite.messenger.utils.state.PhoneModel

class PhoneAdapter(private val listener: PhoneListener): ListAdapter<PhoneModel, PhoneViewHolder>(PhoneDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutItemPhoneBinding.inflate(layoutInflater, parent, false)
        return PhoneViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PhoneViewHolder, position: Int) {
        holder.bind(getItem(holder.layoutPosition))
    }
}