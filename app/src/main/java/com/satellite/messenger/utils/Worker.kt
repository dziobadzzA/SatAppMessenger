package com.satellite.messenger.utils

import com.satellite.messenger.utils.state.ServerMessage

interface Worker {
    fun worker(temp: ServerMessage = ServerMessage())
}