package com.anavol.auth_application.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel(private val authService: AuthService) : ViewModel() {

    var login = MutableLiveData<String>("")

    var photo = MutableLiveData<String>("")

    var isLogged = MutableLiveData<Boolean>(false)

    fun onClickLogin() {
        val user = User(login.value.toString(), photo.value.toString())
        authService.passLoggedUser(user)
    }

    fun onClickVkAuth() {
        authService.authVK()
    }
}
