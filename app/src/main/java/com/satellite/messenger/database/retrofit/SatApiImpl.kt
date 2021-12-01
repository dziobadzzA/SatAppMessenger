package com.satellite.messenger.database.retrofit

import com.satellite.messenger.utils.state.Satellite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SatApiImpl {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://satmessenger-default-rtdb.europe-west1.firebasedatabase.app")
        .build()

    private val SatService = retrofit.create(SatApi::class.java)

    suspend fun getListOfSats(): List<Satellite> {
        return withContext(Dispatchers.IO) {
            try {
                SatService.getListOfSats()
            } catch (e: Exception) {
                listOf()
            }
        }
    }

}