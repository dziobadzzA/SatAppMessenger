package com.satellite.messenger.utils.state

import java.io.Serializable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Satellite(
    @Json(name="TV") var TV:String = "",
    @Json(name="beacon") var beacon:String = "",
    @Json(name="coverage") var coverage:String = "",
    @Json(name="firma") var firma:String = "",
    @Json(name="labelimage") var labelimage:String = "",
    @Json(name="name") var name:String = "",
    @Json(name="point") var point:String = "",
    @Json(name="video") var video:String = ""
):Serializable