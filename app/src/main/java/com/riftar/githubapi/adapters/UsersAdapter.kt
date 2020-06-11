package com.riftar.githubapi.adapters

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.riftar.githubapi.R
import com.riftar.githubapi.db.entities.User
import kotlinx.android.synthetic.main.item_user.view.*

class UsersAdapter: RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null
    private var listItem: List<User> = ArrayList()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: User) {
            val tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
            with(itemView) {
                Glide.with(itemView.context)
                    .load(item.avatarUrl)
                    .transform(CenterCrop())
                    .into(ivUserImage)
            }
            tvUserName.text = item.login
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listItem[position])
    }
    fun setItem(listItem: List<User>){
        if (listItem != null) {
            Log.d(ContentValues.TAG, "setItem")
            this.listItem = listItem
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}