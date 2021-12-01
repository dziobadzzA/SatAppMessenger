package com.satellite.messenger.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "messages")
data class Messages (
    @PrimaryKey(autoGenerate = true)
    var Id: Long = 0L,

    @ColumnInfo(name = "email")
    var email: String = "",

    @ColumnInfo(name = "message")
    var message: ByteArray? = null,

    @ColumnInfo(name = "datetime")
    var datetime: Long = System.currentTimeMillis()): Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Messages

        if (Id != other.Id) return false
        if (email != other.email) return false
        if (message != null) {
            if (other.message == null) return false
            if (!message.contentEquals(other.message)) return false
        } else if (other.message != null) return false
        if (datetime != other.datetime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + (message?.contentHashCode() ?: 0)
        result = 31 * result + datetime.hashCode()
        return result
    }
}
