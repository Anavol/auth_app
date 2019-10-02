package com.anavol.auth_application

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel(): ViewModel() {
    var login = MutableLiveData<String>()
        .apply {
            value = ""
        }
    var isLogged = MutableLiveData<Boolean>()
        .apply {
            value = false

        }
}