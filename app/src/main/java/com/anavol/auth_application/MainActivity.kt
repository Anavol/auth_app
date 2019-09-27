package com.anavol.auth_application

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vk.sdk.util.VKUtil
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.vk.sdk.VKSdk
import com.vk.sdk.VKScope
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.api.VKApi
import com.vk.sdk.api.VKError
import com.vk.sdk.api.model.VKApiOwner
import com.vk.sdk.api.model.VKApiPhoto
import com.vk.sdk.api.model.VKApiUser
import kotlinx.android.synthetic.main.activity_main.*
import com.vk.sdk.api.VKRequest
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.vk.sdk.api.VKResponse
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // val fingerprints = VKUtil.getCertificateFingerprint(this, this.packageName)
        VKSdk.initialize(this.applicationContext)
        VKSdk.login(this, VKScope.STATS)
        var request = VKApi.users().get()
        request.executeWithListener(object: VKRequest.VKRequestListener() {

            override fun onComplete(response: VKResponse) {
                textView.text = response.responseString
            }
           override fun onError(error: VKError) {
                //Do error stuff
            }
            override fun attemptFailed(request: VKRequest,attemptNumber: Int, totalAttempts: Int) {
                //I don't really believe in progress
            }
        });
    }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {

                    override fun onResult(res: VKAccessToken) {
                        var id = res.userId
                        textView2.text = res.userId
                    }
                    override
                    fun onError(error: VKError ) {
                        textView2.text = "Error"
                    }
                })) {
            super.onActivityResult(requestCode, resultCode, data)
        }


    }

}
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
    }
}
