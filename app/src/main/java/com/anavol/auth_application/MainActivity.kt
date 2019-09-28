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
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import com.vk.sdk.VKAccessToken.ACCESS_TOKEN
import com.vk.sdk.api.*
import com.vk.sdk.api.model.*
import com.vk.sdk.api.model.VKList
import com.vk.sdk.api.model.VKApiUserFull




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // val fingerprints = VKUtil.getCertificateFingerprint(this, this.packageName)
        VKSdk.initialize(this.applicationContext)
        VKSdk.login(this, VKScope.STATS)
        var request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,"photo_200"))
        val mainIntent = Intent(this, Activity2::class.java)
        request.executeWithListener(object: VKRequest.VKRequestListener() {
    //access_token -> 289bb688ac1177b6fb45ff5eba673757766f16c23d95ad43ddfc53b7ab5fe86c1f0d3d53a255684f10827
            override fun onComplete(response: VKResponse) {
                val responseParsed = (response.parsedModel as VKList<VKApiUserFull>)[0]
                val user = User(responseParsed.first_name + " " + responseParsed.last_name, responseParsed.photo_200, request.preparedParameters["access_token"].toString() )

                startActivity(mainIntent.putExtra("user", user))
            }
           override fun onError(error: VKError) {
                //Do error stuff
            }
            override fun attemptFailed(request: VKRequest,attemptNumber: Int, totalAttempts: Int) {
                //I don't really believe in progress
            }
        })

    }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {

                    override fun onResult(res: VKAccessToken) {

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
