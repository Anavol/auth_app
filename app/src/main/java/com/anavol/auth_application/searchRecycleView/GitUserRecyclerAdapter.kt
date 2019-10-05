package com.anavol.auth_application.searchRecycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anavol.auth_application.R
import com.anavol.auth_application.gitSearchTools.GitUser
import kotlinx.android.synthetic.main.user_list_item.view.*

class GitUserRecyclerAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<GitUser> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GitUserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.user_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GitUserViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(userList: List<GitUser>) {
        items = userList
    }
}

class GitUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val user_name = itemView.user_name
    val user_url = itemView.user_link

    fun bind(gitUser: GitUser) {
        user_name.text = gitUser.login
        user_url.text = gitUser.html_url
    }
}