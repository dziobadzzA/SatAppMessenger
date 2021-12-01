package com.satellite.messenger.ui.phone.adapter

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.satellite.messenger.utils.state.PhoneModel

class PhoneDiffCallback:DiffUtil.ItemCallback<PhoneModel>() {

    override fun areItemsTheSame(oldItem: PhoneModel, newItem: PhoneModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: PhoneModel, newItem: PhoneModel): Boolean {
        return oldItem == newItem
    }

    @Nullable
    @Override
    override fun getChangePayload(oldItem: PhoneModel, newItem: PhoneModel): Any? {
        return super.getChangePayload(oldItem, newItem)
    }
}