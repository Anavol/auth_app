package com.anavol.auth_application

import android.app.Application
import com.vk.sdk.VKSdk

class AuthApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
    }
}