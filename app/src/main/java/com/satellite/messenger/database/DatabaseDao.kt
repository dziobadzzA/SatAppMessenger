package com.satellite.messenger.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.satellite.messenger.database.entity.Auth
import com.satellite.messenger.database.entity.Messages
import com.satellite.messenger.database.entity.Profile

@Dao
interface DatabaseDao {

    @Insert
    suspend fun insertMessages(messages: Messages)

    @Query("SELECT * from messages ORDER BY Id DESC")
    suspend fun getMessages(): List<Messages>

    @Query("SELECT * from messages WHERE Id=(SELECT MAX(Id) FROM messages) ORDER BY Id ")
    suspend fun getLastMessage(): Messages


    @Insert
    suspend fun insertProfile(profile: Profile)

    @Query("DELETE FROM profile WHERE Id=1")
    suspend fun deleteProfile()

    @Query("UPDATE profile SET stateStorage = :state")
    suspend fun updateStateProfile(state:Boolean)

    @Query("SELECT * FROM profile WHERE Id=1")
    suspend fun getProfile():Profile

    @Query("UPDATE profile SET placeStorage = :url")
    suspend fun updatePlaceStorageProfile(url: String)


    @Insert
    suspend fun insertAuth(auth: Auth)

    @Query("UPDATE auth SET initKey = :initKey, keyAuth = :keyAuth")
    suspend fun updateAuthLogin(initKey:ByteArray?, keyAuth: ByteArray?)

    @Query("UPDATE auth SET keyMessage = :keyMessages")
    suspend fun updateKeyMessages(keyMessages:ByteArray?)

    @Query("SELECT * FROM auth WHERE Id=1")
    suspend fun getAuth():Auth

    @Query("DELETE FROM auth WHERE Id=1")
    suspend fun deleteAuth()
}