package com.anavol.auth_application

import androidx.lifecycle.MutableLiveData

class User (
    var login: MutableLiveData<String>,
    var photo: MutableLiveData<String>,
    var token: MutableLiveData<String>
)
{

}