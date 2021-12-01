package com.satellite.messenger.ui.messages

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.satellite.messenger.database.Repository
import com.satellite.messenger.database.entity.Messages
import com.satellite.messenger.utils.keyLength
import com.satellite.messenger.utils.state.ServerMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import com.satellite.messenger.utils.FirebaseObserver
import com.satellite.messenger.utils.Worker


class MessagesViewModel @Inject constructor(val repository: Repository): ViewModel(), Worker {

    private var key: ByteArray? = null
    private var lastMessage = ServerMessage(datetime = 0)
    private var datetime = 0L

    private var _messages: MutableLiveData<List<ServerMessage>>? = MutableLiveData<List<ServerMessage>>()
    val messages: LiveData<List<ServerMessage>>? get() = _messages

    fun returnLastMessage() = lastMessage

    fun controlMessage(email:String, text:String):Boolean {

        var returnResult=false

        if (text.isNotEmpty()) {
            if (correctKey()) {
                val textMessages = key?.let { encoderText(text, it) }

                if (textMessages != null) {
                    val newMessageToSend = Messages(email = email, message = textMessages)
                    addMessage(newMessageToSend)
                    returnResult = true
                }
            }
        }

        return returnResult
    }

    private fun addMessage(data:Messages) {
        val sendMessages = ServerMessage(email = data.email, datetime = data.datetime)
        data.message?.let { sendMessages.convertSendMessage(it) }
        FirebaseDatabase.getInstance().reference.child("text").push().setValue(sendMessages)
        displayAllMessages()
    }

    private fun correctKey():Boolean {
        if (key != null)
            if (key!!.size == keyLength)
                return true
        return false
    }

    fun getKey() {
        GlobalScope.launch(Dispatchers.IO) {
            key = repository.getRoomDatabase().getAuth().keyMessages
            val testQuery = repository.getRoomDatabase().getLastMessage()
            if (testQuery != null) {
                datetime = testQuery.datetime
                lastMessage = ServerMessage(email = testQuery.email, datetime = testQuery.datetime)
                testQuery.message?.let { lastMessage.convertSendMessage(it) }
            }
            getLastMessage()
            displayAllMessages()
        }
    }

    private fun getLastMessage() {
        GlobalScope.launch(Dispatchers.Main) {
            val tempMessage = repository.getRoomDatabase().getLastMessage()
            if (tempMessage != null) {
                lastMessage = ServerMessage(email = tempMessage.email, datetime = tempMessage.datetime)
                tempMessage.message?.let { lastMessage.convertSendMessage(it) }
            }
        }
    }

    private fun encoderText(text: String, key:ByteArray): ByteArray? = cipher(Cipher.ENCRYPT_MODE, text = text.toByteArray(), key = key)

    private fun decoderText(text: ByteArray, key: ByteArray):String {
        val test = cipher(Cipher.DECRYPT_MODE, text = text, key = key)
        return if (test == null)
            ""
        else
            String(test)
    }

    private fun cipher(mode:Int, text: ByteArray, key: ByteArray): ByteArray? {

        var tempEncodedBytes: ByteArray? = null

        try {
            val keyCipher = SecretKeySpec(key, "AES")
            val c: Cipher = Cipher.getInstance("AES")
            c.init(mode, keyCipher)
            tempEncodedBytes = c.doFinal(text)
        } catch (e: Exception) {
            Log.e("Crypto", "AES error")
        }

        return tempEncodedBytes
    }

    private fun displayAllMessages() {

        GlobalScope.launch(Dispatchers.Main) {
            val allMessages = repository.getRoomDatabase().getMessages()
            val listMessages: MutableList<ServerMessage> = mutableListOf()
            for (i in allMessages.indices) {
                val addMessage = key?.let {
                    allMessages[i].message?.let { it1 ->
                        decoderText(
                            it1,
                            it
                        )
                    }
                }?.let { ServerMessage(email = allMessages[i].email, message = it, datetime = allMessages[i].datetime) }
                if (addMessage != null) {
                    listMessages.add(addMessage)
                }
            }
            _messages?.value = listMessages.toList()
        }

        FirebaseObserver {
            worker(it)
        }

    }

    private fun insertMessage(message: ServerMessage) {
        GlobalScope.launch(Dispatchers.Default) {
            repository.getRoomDatabase().insertMessages(Messages(email = message.email,
                message = message.convertReceiveMessage(message.message), datetime = message.datetime))
        }
    }

    override fun worker(temp: ServerMessage) {
        if (lastMessage != temp && (lastMessage.datetime <= temp.datetime)) {
            insertMessage(temp)
            lastMessage = temp
            displayAllMessages()
        }

    }

}