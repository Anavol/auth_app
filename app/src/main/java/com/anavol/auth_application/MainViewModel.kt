package com.anavol.auth_application

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var login = MutableLiveData<String>()
        .apply {
            value = ""
        }
    var photoLocal = MutableLiveData<String>()
        .apply {
            value = ""
        }
}