package com.anavol.auth_application.searchRecycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anavol.auth_application.R
import com.anavol.auth_application.gitSearchTools.GitUser

class CustomAdapter(val userList: ArrayList<GitUser>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: GitUser) {
            val textViewName = itemView.findViewById(R.id.user_name) as TextView
            val textViewUrl  = itemView.findViewById(R.id.user_link) as TextView
            textViewName.text = user.login
            textViewUrl.text = user.url
        }
    }
}