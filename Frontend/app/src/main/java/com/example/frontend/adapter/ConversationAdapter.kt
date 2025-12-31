package com.example.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.model.ConversationItem
import com.example.frontend.utils.formatConversationTime

class ConversationAdapter(
    private val onClick: (ConversationItem) -> Unit
) : ListAdapter<ConversationItem, ConversationAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTv: TextView = itemView.findViewById(R.id.tv_conv_name)
        private val lastMsgTv: TextView = itemView.findViewById(R.id.tv_conv_last)
        private val timeTv: TextView = itemView.findViewById(R.id.tv_conv_time)

        private val avatarTv: TextView = itemView.findViewById(R.id.tv_avatar)

        fun bind(item: ConversationItem) {
            nameTv.text = item.name
            lastMsgTv.text = item.lastMessage
            timeTv.text = formatConversationTime(item.lastMessageTime)
            avatarTv.text = item.username
                .trim()
                .firstOrNull()
                ?.uppercase()
                ?: "?"
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ConversationItem>() {
            override fun areItemsTheSame(oldItem: ConversationItem, newItem: ConversationItem): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: ConversationItem, newItem: ConversationItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}