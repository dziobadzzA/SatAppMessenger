package com.satellite.messenger.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth")
data class Auth(
    @PrimaryKey(autoGenerate = true)
    var Id: Long = 0L,

    @ColumnInfo(name = "initKey")
    var initKey: ByteArray? = null,

    @ColumnInfo(name = "keyAuth")
    var keyAuth: ByteArray? = null,

    @ColumnInfo(name = "keyMessage")
    var keyMessages: ByteArray? = null) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Auth

        if (Id != other.Id) return false
        if (initKey != null) {
            if (other.initKey == null) return false
            if (!initKey.contentEquals(other.initKey)) return false
        } else if (other.initKey != null) return false
        if (keyAuth != null) {
            if (other.keyAuth == null) return false
            if (!keyAuth.contentEquals(other.keyAuth)) return false
        } else if (other.keyAuth != null) return false
        if (keyMessages != null) {
            if (other.keyMessages == null) return false
            if (!keyMessages.contentEquals(other.keyMessages)) return false
        } else if (other.keyMessages != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Id.hashCode()
        result = 31 * result + (initKey?.contentHashCode() ?: 0)
        result = 31 * result + (keyAuth?.contentHashCode() ?: 0)
        result = 31 * result + (keyMessages?.contentHashCode() ?: 0)
        return result
    }

}