package com.anavol.auth_application.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var login = MutableLiveData<String>("")
    var photo = MutableLiveData<String>("")
}