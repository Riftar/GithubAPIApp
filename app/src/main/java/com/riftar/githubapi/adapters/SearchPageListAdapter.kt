package com.riftar.githubapi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.riftar.githubapi.R
import com.riftar.githubapi.db.entities.User
import com.riftar.githubapi.repository.NetworkState
import kotlinx.android.synthetic.main.item_user.view.*

class SearchPageListAdapter : PagedListAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback()) {
    var itemClickCallback:OnItemClickCallback? = null
    val USER_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    private var networkState : NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view:View
        if (viewType == USER_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.item_user, parent, false)
            return UserItemViewHolder(view)
        } else{
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == USER_VIEW_TYPE){
            (holder as UserItemViewHolder).bind(getItem(position), itemClickCallback)
        } else{
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    class UserItemViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun bind(
            item: User?,
            itemClickCallback: OnItemClickCallback?
        ){
            val tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
            val tvNumber = itemView.findViewById<TextView>(R.id.tvNumber)
            with(itemView) {
                Glide.with(itemView.context)
                    .load(item?.avatarUrl)
                    .transform(CenterCrop())
                    .into(ivUserImage)
            }
            tvUserName.text = item?.login
            tvNumber.text = layoutPosition.toString()
            itemView.setOnClickListener {
                if (item != null) {
                    itemClickCallback?.onItemClicked(item)
                }
            }
        }
    }

    class NetworkStateItemViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun bind(networkState: NetworkState?){
            val progressBarItem = itemView.findViewById<ProgressBar>(R.id.progressBarNetworkItem)
            val ivErrorItem = itemView.findViewById<ImageView>(R.id.ivErrorNetworkItem)
            val tvEndOfList = itemView.findViewById<TextView>(R.id.tvEndOfList)
            if (networkState != null && networkState == NetworkState.LOADING){
                progressBarItem.visibility = View.VISIBLE
            } else{
                progressBarItem.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR){
                ivErrorItem.visibility = View.VISIBLE
                tvEndOfList.visibility = View.GONE
            }
            else if (networkState != null && networkState == NetworkState.END_OF_LIST){
                ivErrorItem.visibility = View.GONE
                tvEndOfList.text = networkState.msg
            } else{
                ivErrorItem.visibility = View.GONE
                tvEndOfList.visibility = View.GONE
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.equals(newItem)
        }
    }

    private fun hasExtraRow(): Boolean{
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount -1){
            NETWORK_VIEW_TYPE
        } else{
            USER_VIEW_TYPE
        }
    }

    fun setNetworkState(newNetworkState: NetworkState){
        val previousNetworkState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow){
            if (hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            } else{
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousNetworkState != newNetworkState) {
            notifyItemChanged(itemCount-1)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.itemClickCallback = onItemClickCallback
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: User?)
    }
}