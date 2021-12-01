package com.satellite.messenger.ui.phone.adapter

import com.satellite.messenger.utils.state.PhoneModel

interface PhoneListener {
    fun call(phone:PhoneModel)
}