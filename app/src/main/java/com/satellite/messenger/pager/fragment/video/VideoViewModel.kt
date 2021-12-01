package com.satellite.messenger.pager.fragment.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class VideoViewModel @Inject constructor(): ViewModel() {

    private var _position = MutableLiveData<Int>()
    val position: LiveData<Int> get() = _position
    var videoUrl = ""
    var play = false

    init {
        _position.value = 0
    }

    fun saveState(position:Int, play:Boolean) {
        _position.value = position
        this.play = play
    }
}