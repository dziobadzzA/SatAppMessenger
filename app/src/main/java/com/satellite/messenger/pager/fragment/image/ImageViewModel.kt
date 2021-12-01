package com.satellite.messenger.pager.fragment.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.satellite.messenger.utils.state.Satellite
import javax.inject.Inject
import kotlin.properties.Delegates

class ImageViewModel @Inject constructor():ViewModel() {

   var list: MutableList<String> = mutableListOf()
   var position by Delegates.notNull<Int>()

   fun initList(str:String) {
         val temp = str.split(";")
         for (i in temp)
            if (i.isNotEmpty())
               list.add(i)

         position = 0
   }


}