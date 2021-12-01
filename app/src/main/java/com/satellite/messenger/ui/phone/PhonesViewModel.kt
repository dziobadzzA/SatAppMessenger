package com.satellite.messenger.ui.phone

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.satellite.messenger.utils.state.PhoneModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhonesViewModel @Inject constructor(): ViewModel() {

    private var _itemPhone: MutableLiveData<List<PhoneModel>>? = MutableLiveData<List<PhoneModel>>()
    val itemPhone: LiveData<List<PhoneModel>>? get() = _itemPhone

    fun getListPhone() {

        GlobalScope.launch(Dispatchers.IO) {
            FirebaseDatabase.getInstance().reference.child("phone").get().addOnSuccessListener {
                val resultRequest = it.value as HashMap<*, *>
                val items: MutableList<PhoneModel> = mutableListOf()
                for((key, value) in resultRequest) {
                     items.add(PhoneModel(name = key.toString(), phone = value.toString()))
                }
                _itemPhone?.value = items.toList()
            }
        }

    }

    fun controlCalling(pack:PackageManager):Boolean {
        val telephonySupported = pack.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
        val gsmSupported = pack.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_GSM)
        val cdmaSupported = pack.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA)
        return telephonySupported && gsmSupported && cdmaSupported
    }

}