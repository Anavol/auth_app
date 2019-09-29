package com.anavol.auth_application

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vk.sdk.VKSdk
import com.vk.sdk.VKScope
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import android.os.Handler
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.anavol.auth_application.databinding.ActivityLoginBinding
import com.squareup.picasso.Picasso
import com.vk.sdk.VKSdk.login
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKList
import com.vk.sdk.api.model.VKApiUserFull
import com.vk.sdk.util.VKUtil
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Runnable


class LoginActivity : AppCompatActivity() {

    private lateinit var mDbWorkerThread: DbWorkerThread
    private var mDb: UserDataBase? = null
    var userData = UserData()
    private val mUiHandler = Handler()
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainIntent = Intent(this, Activity2::class.java)
      //  val fingerprints = VKUtil.getCertificateFingerprint(this, this.packageName)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = UserDataBase.getInstance(this)
        fetchUserDataFromDb()
        if (userData.id != null ) {
            val user = User(userData.name, userData.photo)
            viewModel.login.value = user.login
            Picasso.get()
                .load(user.photo)
                .into(profilePic)
          //  startActivity(mainIntent.putExtra("user", user))
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
                    insertUserDataInDb(userData)
                    userData = UserData()
                     fetchUserDataFromDb()
                    val user = User(userData.name, userData.photo)
                    startActivity(mainIntent.putExtra("user", user))

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

    private fun fetchUserDataFromDb() {

        val task = Runnable {
            val userDataList =
                mDb?.UserDataDao()?.getAll()
                if (userDataList == null || userDataList?.size == 0) {
                }
                else {
                    userData.id = userDataList?.get(0).id
                    userData.name = userDataList?.get(0).name
                    userData.photo = userDataList?.get(0).photo
                    userData.token = userDataList?.get(0).token
                    userData.socialNetwork = userDataList?.get(0).socialNetwork
               }
        }
        mDbWorkerThread.postTask(task)
    }
    
    private fun insertUserDataInDb(UserData: UserData) {
        val task = Runnable { mDb?.UserDataDao()?.insert(UserData) }
        mDbWorkerThread.postTask(task)
    }
    
    override fun onDestroy() {
        UserDataBase.destroyInstance()
        mDbWorkerThread.quit()
        super.onDestroy()
    }
}
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
    }
}
