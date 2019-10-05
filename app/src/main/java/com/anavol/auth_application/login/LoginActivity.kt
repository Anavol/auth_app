package com.anavol.auth_application.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.anavol.auth_application.R
import com.anavol.auth_application.databinding.ActivityLoginBinding
import com.anavol.auth_application.main.MainActivity
import com.anavol.auth_application.DbTools.DbTools.Companion.fetchUserDataFromDb
import com.anavol.auth_application.DbTools.DbTools.Companion.insertUserDataInDb
import com.anavol.auth_application.DbTools.UserData
import com.anavol.auth_application.DbTools.UserDataBase
import com.squareup.picasso.Picasso
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiUserFull
import com.vk.sdk.api.model.VKList
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity(), AuthService {

    private var mDb: UserDataBase? = null
    var userData = UserData()
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory { LoginViewModel(this) })
            .get(LoginViewModel(this)::class.java)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_login
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mDb = UserDataBase.getInstance(this)
        GlobalScope.launch(Dispatchers.Main) {
            fetchUserDataFromDb(mDb, userData)
            if (userData.id != null) {
                val user = User(userData.name, userData.photo)
                viewModel.login.value = user.login
                viewModel.photo.value = userData.photo
                viewModel.isLogged.value = true
                Picasso.get()
                    .load(user.photo)
                    .into(profilePic)
            } else {
                viewModel.isLogged.value = false
                VKSdk.logout()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.Main) {
            fetchUserDataFromDb(mDb, userData)
            if (userData.id != null) {
                val user = User(userData.name, userData.photo)
                viewModel.login.value = user.login
                viewModel.photo.value = userData.photo
                viewModel.isLogged.value = true
                Picasso.get()
                    .load(user.photo)
                    .into(profilePic)
            } else {
                viewModel.isLogged.value = false
                VKSdk.logout()
            }
        }
    }

    override fun passLoggedUser(user: User) {
        val mainIntent = Intent(this, MainActivity::class.java)
        viewModel.isLogged.value = true
        startActivity(mainIntent.putExtra("user", user))
    }

    override fun authVK() {
        VKSdk.initialize(this.applicationContext)
        VKSdk.login(this, VKScope.STATS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(
                requestCode,
                resultCode,
                data,
                object : VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken) {
                        var request =
                            VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200"))
                        request.executeWithListener(object : VKRequest.VKRequestListener() {
                            override fun onComplete(response: VKResponse) {
                                processVkResponse(response, request)
                            }

                            override fun onError(error: VKError) = Unit

                            override fun attemptFailed(
                                request: VKRequest,
                                attemptNumber: Int,
                                totalAttempts: Int
                            ) {
                            }
                        })
                    }

                    override fun onError(error: VKError) {
                    }
                })
        ) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun processVkResponse(response: VKResponse, request: VKRequest) {
        val responseParsed = (response.parsedModel as VKList<VKApiUserFull>)[0]
        userData.name = responseParsed.first_name + " " + responseParsed.last_name
        userData.photo = responseParsed.photo_200
        userData.token = request.preparedParameters["access_token"].toString()
        userData.socialNetwork = "VK"
        GlobalScope.launch(Dispatchers.Main) {
            insertUserDataInDb(mDb, userData)
            userData = UserData()
            fetchUserDataFromDb(mDb, userData)
            var user = User(userData.name, userData.photo)
            passLoggedUser(user)
        }
    }

    private inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
        }
}


