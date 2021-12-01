package com.satellite.messenger.pager.fragment

import com.satellite.messenger.utils.state.Satellite

interface DataItem {
    fun setItem(channel:Satellite)
    fun deleteItem(position:Int)
    fun getItem(): Satellite
    fun getItems():List<Satellite>
}