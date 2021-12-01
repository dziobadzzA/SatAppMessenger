package com.satellite.messenger.pager.fragment.info.adapter

import com.satellite.messenger.utils.state.Satellite

interface InfoListener {
    fun show(channel:Satellite)
}