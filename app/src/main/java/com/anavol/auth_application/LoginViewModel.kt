package com.anavol.auth_application

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anavol.auth_application.userDBTools.DbTools
import com.anavol.auth_application.userDBTools.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
