package com.satellite.messenger.ui.phone.adapter

import androidx.recyclerview.widget.RecyclerView
import com.satellite.messenger.databinding.LayoutItemPhoneBinding
import com.satellite.messenger.utils.state.PhoneModel

class PhoneViewHolder(
    private val binding: LayoutItemPhoneBinding,
    private val listener: PhoneListener,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(phone: PhoneModel) {

        binding.imageUser.setOnClickListener {
            listener.call(phone)
        }
        binding.textUser.text = phone.name
        binding.textPhone.text = phone.phone
    }
}