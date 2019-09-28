package com.anavol.auth_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_2.*
import kotlinx.android.synthetic.main.activity_main.*

class Activity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        val user = intent.getParcelableExtra<User>("user")
        textName.text = user.login
        Picasso.get()
            .load(user.photo)
            .into(imageAvatar)
    }
}
