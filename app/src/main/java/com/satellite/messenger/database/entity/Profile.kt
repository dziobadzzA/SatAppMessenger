package com.satellite.messenger.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "profile")
data class Profile (
    @PrimaryKey(autoGenerate = true)
    var Id: Long = 0L,

    @ColumnInfo(name = "stateStorage")
    var state: Boolean = false,

    @ColumnInfo(name = "placeStorage")
    var iconUrl: String = ""): Serializable