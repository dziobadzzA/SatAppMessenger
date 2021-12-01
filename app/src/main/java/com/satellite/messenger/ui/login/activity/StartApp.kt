package com.satellite.messenger.ui.login.activity

import com.satellite.messenger.utils.state.UserModel

interface StartApp {
    fun startApp(userModel: UserModel)
}