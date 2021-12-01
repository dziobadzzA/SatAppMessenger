package com.satellite.messenger.database.retrofit

import com.satellite.messenger.utils.state.Satellite
import retrofit2.http.GET

interface SatApi {
    @GET("/Satellite/.json")
    suspend fun getListOfSats(): List<Satellite>
}