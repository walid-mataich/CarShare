package com.example.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.model.Message
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MessageAdapter(val conversationUserId: Long) : ListAdapter<Message, MessageAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val view = if (viewType == VIEW_TYPE_SENT) {
            inflater.inflate(R.layout.item_message_sent, parent, false)
        } else {
            inflater.inflate(R.layout.item_message_received, parent, false)
        }
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val msg = getItem(position)

        return if (msg.senderId == conversationUserId) VIEW_TYPE_RECEIVED else VIEW_TYPE_SENT
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentTv: TextView = itemView.findViewById(R.id.tv_message_content)
        private val timeTv: TextView? = itemView.findViewById(R.id.tv_message_time)

        fun bind(msg: Message) {
            contentTv.text = msg.content
            timeTv?.text = formatTimestamp(msg.sentAt)
        }

        private fun formatTimestamp(ts: String): String {
            return try {
                val instant = Instant.parse(ts)
                val zoneId = ZoneId.systemDefault()

                val messageTime = instant.atZone(zoneId)
                val now = ZonedDateTime.now(zoneId)

                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                val dateFormatter = DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault())

                when {
                    messageTime.toLocalDate() == now.toLocalDate() -> {

                        messageTime.format(timeFormatter)
                    }
                    messageTime.toLocalDate() == now.minusDays(1).toLocalDate() -> {

                        "Hier · ${messageTime.format(timeFormatter)}"
                    }
                    else -> {

                        "${messageTime.format(dateFormatter)} · ${messageTime.format(timeFormatter)}"
                    }
                }
            } catch (e: Exception) {
                ""
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2

        val DIFF = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }
}