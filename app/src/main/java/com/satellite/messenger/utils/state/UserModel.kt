package com.satellite.messenger.utils.state

import java.io.Serializable

data class UserModel(var Mail: String = "this@email.com",
                     var link: String? = null): Serializable