package com.anavol.auth_application

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    var login = MutableLiveData<String>()
        .apply {
            value = ""
        }
    val searchString = MutableLiveData<String>()
        .apply {
            value = ""
        }

}