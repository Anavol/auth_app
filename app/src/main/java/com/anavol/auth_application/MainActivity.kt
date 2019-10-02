package com.anavol.auth_application

import android.content.Intent
import android.os.Bundle
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.anavol.auth_application.gitSearchTools.GithubApiService
import com.anavol.auth_application.searchRecycleView.GitUserRecyclerAdapter
import com.anavol.auth_application.userDBTools.DbTools
import com.anavol.auth_application.userDBTools.UserDataBase
import com.anavol.auth_application.databinding.ActivityMainBinding
import com.anavol.auth_application.databinding.ContentMainBinding
import com.anavol.auth_application.databinding.NavHeaderMainBinding
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: MainViewModel
    private lateinit var gitUserAdapter: GitUserRecyclerAdapter
    private val gitApiServe by lazy {
       GithubApiService.create()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = intent.getParcelableExtra<User>("user")
        val mDb = UserDataBase.getInstance(this)
        val loginIntent = Intent(this, LoginActivity::class.java)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        val navBind: NavHeaderMainBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.nav_header_main,
            binding.navView,
            false
        )
        binding.navView.addHeaderView(navBind.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        navBind.viewModel = viewModel

        val contentViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val contentBind: ContentMainBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.content_main,
            binding.drawerLayout,
            false
        )
        contentBind.viewModel = contentViewModel

        Picasso.get()
            .load(user.photo)
            .into(navBind.profilePic)
        viewModel.login.value = user.login

        navBind.logout.setOnClickListener {
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
        gitUserAdapter = GitUserRecyclerAdapter()

        btnSearch.setOnClickListener {
            var searchString = searchField.text.toString()
            beginSearch(searchString = searchString)
            contentBind.recyclerView
                .apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = gitUserAdapter
                }
        }
    }


    private fun beginSearch(searchString: String) {
        gitApiServe.search(searchString, 100)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                        gitUserAdapter.submitList(result.items)
                        gitUserAdapter.notifyDataSetChanged()
                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                }
            )

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

}
