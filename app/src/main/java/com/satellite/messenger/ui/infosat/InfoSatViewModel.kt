package com.satellite.messenger.ui.infosat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satellite.messenger.database.retrofit.SatApiImpl
import com.satellite.messenger.utils.state.Satellite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class InfoSatViewModel @Inject constructor(): ViewModel(){

    private val _items = MutableLiveData<MutableList<Satellite>>()
    val items: LiveData<MutableList<Satellite>> get() = _items

    var item: Satellite = Satellite()

    fun getItem() {
        viewModelScope.launch(Dispatchers.Main) {
            _items.value = SatApiImpl.getListOfSats().toMutableList()
            setItem(0)
        }
    }

    private fun setItem(position:Int) {
        if ((position < items.value?.size ?: 0) || (position >= 0))
            item = items.value?.get(position)!!
    }

}