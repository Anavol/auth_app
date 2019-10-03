package com.anavol.auth_application.login

import com.anavol.auth_application.userDBTools.UserData

interface AuthService {
    fun authVK()
    fun passLoggedUser(user: User)
}