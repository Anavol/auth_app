package com.anavol.auth_application

import android.app.Application
import com.vk.sdk.VKSdk
import com.vk.sdk.util.VKUtil

class AuthApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
        //val fingerprints = VKUtil.getCertificateFingerprint(this, this.packageName)
}
}