package com.satellite.messenger.utils.state

import java.io.Serializable

data class ServerMessage(
    val email: String = "", var message: String = "",
    val datetime: Long = System.currentTimeMillis()):Serializable {

    fun convertSendMessage(send:ByteArray) {

        var result = ""

        for (i in send.indices) {
            result += send[i].toString()
            if (i != send.size - 1)
                result += ","
        }

        message = result
    }

    fun convertReceiveMessage(str:String): ByteArray? {

        var result:MutableList<Byte>? = null
        val temp = str.split(",")

        if (temp.isNotEmpty())
            result = mutableListOf()

        for (i in temp.indices) {
            result?.add(temp[i].toByte())
        }

        return result?.toByteArray()
    }

}