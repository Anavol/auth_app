package com.anavol.auth_application

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vk.sdk.VKSdk
import com.vk.sdk.VKScope
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.anavol.auth_application.userDBTools.DbTools.Companion.fetchUserDataFromDb
import com.anavol.auth_application.userDBTools.DbTools.Companion.insertUserDataInDb
import com.anavol.auth_application.userDBTools.UserData
import com.anavol.auth_application.userDBTools.UserDataBase
import com.anavol.auth_application.databinding.ActivityLoginBinding
import com.squareup.picasso.Picasso
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKList
import com.vk.sdk.api.model.VKApiUserFull
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private var mDb: UserDataBase? = null
    var userData = UserData()
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            //val fingerprints = VKUtil.getCertificateFingerprint(this, this.packageName)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val mainIntent = Intent(this, MainActivity::class.java)

        mDb = UserDataBase.getInstance(this)
        GlobalScope.launch(Dispatchers.Main) {
            fetchUserDataFromDb(mDb,userData)
            if (userData.id != null ) {
                val user = User(userData.name, userData.photo)
                viewModel.login.value = user.login
                viewModel.isLogged.value = true
                Picasso.get()
                    .load(user.photo)
                    .into(profilePic)
            }
            else {
                viewModel.isLogged.value = false
                VKSdk.logout()
            }
        }

        btnLogin.setOnClickListener {
            val user = User(userData.name, userData.photo)
            startActivity(mainIntent.putExtra("user", user))
        }

        btnVK.setOnClickListener {
            VKSdk.initialize(this.applicationContext)
            VKSdk.login(this, VKScope.STATS)
            var request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200"))
            request.executeWithListener(object : VKRequest.VKRequestListener() {
                override fun onComplete(response: VKResponse) {
                    val responseParsed = (response.parsedModel as VKList<VKApiUserFull>)[0]
                    userData.name = responseParsed.first_name + " " + responseParsed.last_name
                    userData.photo = responseParsed.photo_200
                    userData.token = request.preparedParameters["access_token"].toString()
                    userData.socialNetwork = "VK"
                        //во вьюМодел?
                    GlobalScope.launch(Dispatchers.Main) {
                        insertUserDataInDb(mDb,userData)
                        userData = UserData()
                        fetchUserDataFromDb(mDb,userData)
                        val user = User(userData.name, userData.photo)
                            //viewModel.onLoginSuccess(userData)
                        viewModel.isLogged.value = true
                        startActivity(mainIntent.putExtra("user", user))
                    }
                }
                override fun onError(error: VKError) {
                    //Do error stuff
                }
                override fun attemptFailed(
                    request: VKRequest,
                    attemptNumber: Int,
                    totalAttempts: Int
                ) {
                    //I don't really believe in progress
                }
            })
        }
    }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken) {
                    }
                    override
                    fun onError(error: VKError ) {
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
