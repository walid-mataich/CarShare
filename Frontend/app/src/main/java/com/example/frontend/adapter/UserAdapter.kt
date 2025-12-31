package com.example.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.model.UserItem

class UserAdapter(
    private val onClick: (UserItem) -> Unit
) : ListAdapter<UserItem, UserAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatarTv: TextView = itemView.findViewById(R.id.tv_avatar)
        private val usernameTv: TextView = itemView.findViewById(R.id.tv_username)

        fun bind(item: UserItem) {
            usernameTv.text = item.username
            avatarTv.text = item.username.trim().firstOrNull()?.uppercase() ?: "?"
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<UserItem>() {
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}