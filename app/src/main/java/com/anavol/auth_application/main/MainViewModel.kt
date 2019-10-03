package com.anavol.auth_application.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var login = MutableLiveData<String>()
        .apply {
            value = ""
        }
    var photo = MutableLiveData<String>()
        .apply {
            value = ""
        }
    val searchString = MutableLiveData<String>()
        .apply {
            value = ""
        }

}