package com.anavol.auth_application

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.anavol.auth_application.databinding.ActivityMainBinding
import com.anavol.auth_application.databinding.NavHeaderMainBinding
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: MainViewModel
    private lateinit var apiService: GithubApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   val user = intent.getParcelableExtra<User>("user")
        apiService = GithubApiService.create()
        val mDb = UserDataBase.getInstance(this)
        val loginIntent = Intent(this, LoginActivity::class.java)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)
        val navBind: NavHeaderMainBinding = DataBindingUtil.inflate(layoutInflater,R.layout.nav_header_main,binding.navView,false)
        binding.navView.addHeaderView(navBind.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        navBind.viewModel = viewModel
        val repository = SearchRepositoryProvider.provideSearchRepository()

       repository.searchUsers("alex")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                    result ->
                var res = result.items
            },
                { error ->
                error.printStackTrace()
            })


      //  Picasso.get()
       //     .load(user.photo)
        //    .into(navBind.imageView)
        viewModel.login.value = "default user"

        navBind.button.setOnClickListener {
            GlobalScope.launch(Dispatchers.Default) {
                DbTools.clearDb(mDb)
                startActivity(loginIntent)
            }
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(
            ), drawerLayout
        )
    }
    object SearchRepositoryProvider {
        fun provideSearchRepository(): GitSearchRepository {
            return GitSearchRepository(apiService = apiService)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

}
