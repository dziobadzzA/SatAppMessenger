package com.satellite.messenger.ui.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.satellite.messenger.database.Repository
import com.satellite.messenger.utils.keyLength
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

class SettingsViewModel @Inject constructor(val repository: Repository): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is master key"
    }
    val text: LiveData<String> = _text

    fun getKey() {

        GlobalScope.launch(Dispatchers.Main) {
            val text = repository.getRoomDatabase().getAuth()
            if (text.keyMessages != null){
                _text.value = String(text.keyMessages!!)
            }
        }

    }

    fun readKey(uri: Uri, context:Context) {

        val br: BufferedReader
        try {
            br = BufferedReader(InputStreamReader(uri.let {
                context.contentResolver.openInputStream(it) }))

            var line: String?
            var outputString = ""
            while (br.readLine().also { line = it } != null) {
                outputString += line
            }

            if (outputString.isNotEmpty() && (outputString.length == keyLength)) {
                _text.value = outputString

                GlobalScope.launch(Dispatchers.IO) {
                    repository.getRoomDatabase().updateKeyMessages(outputString.toByteArray())
                }

            }

            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}