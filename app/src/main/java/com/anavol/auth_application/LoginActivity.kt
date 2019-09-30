package com.anavol.auth_application

import android.app.Application
import android.app.PendingIntent.getActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vk.sdk.VKSdk
import com.vk.sdk.VKScope
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.anavol.auth_application.DbTools.Companion.fetchUserDataFromDb
import com.anavol.auth_application.DbTools.Companion.insertUserDataInDb
import com.anavol.auth_application.databinding.ActivityLoginBinding
import com.squareup.picasso.Picasso
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKList
import com.vk.sdk.api.model.VKApiUserFull
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.android.gms.auth.api.signin.GoogleSignIn
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
import com.google.android.gms.auth.api.signin.GoogleSignInResult





class LoginActivity : AppCompatActivity() {

    private var mDb: UserDataBase? = null
    var userData = UserData()
    private lateinit var viewModel: LoginViewModel
    private val RC_SIGN_IN = 9001
    lateinit var mainIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainIntent = Intent(this, MainActivity::class.java)
      //  val fingerprints = VKUtil.getCertificateFingerprint(this, this.packageName)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mDb = UserDataBase.getInstance(this)
        GlobalScope.launch(Dispatchers.Main) {
            fetchUserDataFromDb(mDb,userData)
            if (userData.id != null ) {
                val user = User(userData.name, userData.photo)
                viewModel.login.value = user.login
                viewModel.isLoged.value = true
                Picasso.get()
                    .load(user.photo)
                    .into(profilePic)
            }
            else {
                viewModel.isLoged.value = false
             //   VKSdk.logout()
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
                    GlobalScope.launch(Dispatchers.Main) {
                        insertUserDataInDb(mDb,userData)
                        userData = UserData()
                        fetchUserDataFromDb(mDb,userData)
                        val user = User(userData.name, userData.photo)
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
        btnGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                override fun onResult(res: VKAccessToken) {
                }
                override
                fun onError(error: VKError ) {
                }
            }))
        if(requestCode == RC_SIGN_IN) {
            val result : GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }
    fun handleSignInResult(signInResult: GoogleSignInResult) {
        if(signInResult.isSuccess) {
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {

                userData.name = acct.displayName.toString()
                userData.photo = acct.photoUrl.toString()
                userData.token = ""
                userData.socialNetwork = "Google"
                GlobalScope.launch(Dispatchers.Main) {
                    insertUserDataInDb(mDb,userData)
                    userData = UserData()
                    fetchUserDataFromDb(mDb,userData)
                    val user = User(userData.name, userData.photo)
                    startActivity(mainIntent.putExtra("user", user))
                }
            }
            val account : GoogleSignInAccount? = signInResult.signInAccount
        } else {
            // Failed
        }
    }



      /*  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken) {
                    }
                    override
                    fun onError(error: VKError ) {
                    }
                }))
            {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
*/

}
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
    }
}
