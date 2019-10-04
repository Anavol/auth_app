package com.anavol.auth_application.login

interface AuthService {
    fun authVK()
    fun passLoggedUser(user: User)
}