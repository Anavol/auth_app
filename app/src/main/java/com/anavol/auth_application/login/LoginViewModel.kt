package com.anavol.auth_application.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel(private val authService: AuthService): ViewModel() {
    var login = MutableLiveData<String>()
        .apply {
            value = ""
        }
    var photo = MutableLiveData<String>()
        .apply {
            value = ""
        }
    var isLogged = MutableLiveData<Boolean>()
        .apply {
            value = false

        }
    fun onClickLogin() {
        val user = User(login.value.toString(), photo.value.toString())
        authService.passLoggedUser(user)
    }

    fun onClickVkAuth(){
        authService.authVK()
    }


    }
