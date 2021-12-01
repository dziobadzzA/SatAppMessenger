package com.satellite.messenger.utils.state


data class StateLogin(var emailError: String? = null,
                      var passwordError: String? = null,
                      var state: Boolean = false)

