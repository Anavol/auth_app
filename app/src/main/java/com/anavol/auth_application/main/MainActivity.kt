package com.anavol.auth_application.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anavol.auth_application.R
import com.anavol.auth_application.databinding.ActivityMainBinding
import com.anavol.auth_application.databinding.ContentMainBinding
import com.anavol.auth_application.databinding.NavHeaderMainBinding
import com.anavol.auth_application.gitSearchTools.GitUser
import com.anavol.auth_application.gitSearchTools.GithubApiService
import com.anavol.auth_application.login.LoginActivity
import com.anavol.auth_application.login.User
import com.anavol.auth_application.searchRecycleView.CustomAdapter
import com.anavol.auth_application.userDBTools.DbTools
import com.anavol.auth_application.userDBTools.UserDataBase
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: MainViewModel
    private lateinit var searchString: String
    private val gitApiServe by lazy {
        GithubApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = intent.getParcelableExtra<User>("user")
        val mDb = UserDataBase.getInstance(this)
        var page = 0
        val loginIntent = Intent(this, LoginActivity::class.java)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, com.anavol.auth_application.R.layout.activity_main
        )
        val navBind: NavHeaderMainBinding = NavHeaderMainBinding.inflate(
            layoutInflater,
            binding.navView,
            false
        )
        binding.navView.addHeaderView(navBind.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        navBind.viewModel = viewModel

        val recyclerView = recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val users = ArrayList<GitUser>()
        val adapter = CustomAdapter(users)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
        if (recyclerView.layoutManager?.childCount?.plus((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition())!!
            == recyclerView.layoutManager?.itemCount?.minus(5)!! )
                   {  //if near fifth item from end
                    searchString = searchField.text.toString()
                    gitApiServe.search(searchString, page++, 30)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { result ->
                                users.addAll(result.items)
                                adapter.notifyDataSetChanged()
                            },
                            { error -> val err = error.message
                            }
                        )
                }
            }
        })

        val contentViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val contentBind: ContentMainBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.content_main,
            binding.drawerLayout,
            false
        )
        contentBind.viewModel = contentViewModel
        viewModel.login.value = user.login
        viewModel.photo.value = user.photo

        Picasso.get()
            .load(viewModel.photo.value)
            .into(navBind.profilePic)
        viewModel.login.value = user.login

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(
            ), drawerLayout
        )
        navBind.logout.setOnClickListener {
            GlobalScope.launch(Dispatchers.Default) {
                DbTools.clearDb(mDb)
                startActivity(loginIntent)
                finish()
            }
        }
        btnSearch.setOnClickListener {

            users.clear()
            page = 0
            adapter.notifyDataSetChanged()
            searchString = searchField.text.toString()
            GlobalScope.launch(Dispatchers.Main) {
                delay(600)
            gitApiServe.search(searchString, page++, 30)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        users.addAll(result.items)
                        adapter.notifyDataSetChanged()
                    },
                    { error -> val er = error.message
                    }
                )
        }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

}
