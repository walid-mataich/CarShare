package com.example.frontend.View

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.ViewModel.ChatViewModel
import com.example.frontend.adapter.ConversationAdapter
import com.example.frontend.adapter.MessageAdapter
import com.example.frontend.model.ConversationItem
import com.example.frontend.model.Message
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MessagesFragment : Fragment() {

    private val viewModel: ChatViewModel by lazy { ChatViewModel() }

    private lateinit var conversationsRv: RecyclerView
    private lateinit var messagesRv: RecyclerView
    private lateinit var searchEdit: EditText
    private lateinit var newChatBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var sendBtn: ImageButton
    private lateinit var messageInput: EditText
    private lateinit var conversationContainer: View
    private lateinit var conversationsContainer: View
    private lateinit var emptyConversationsText: TextView
    private lateinit var convTitle: TextView

    private lateinit var conversationAdapter: ConversationAdapter
    private var messageAdapter: MessageAdapter? = null

    private var currentConversationUserId: Long? = null
    private var allConversations: List<ConversationItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        conversationsRv = view.findViewById(R.id.rv_conversations)
        messagesRv = view.findViewById(R.id.rv_messages)
        searchEdit = view.findViewById(R.id.et_search)
        newChatBtn = view.findViewById(R.id.btn_new_chat)
        backBtn = view.findViewById(R.id.btn_back)
        sendBtn = view.findViewById(R.id.btn_send)
        messageInput = view.findViewById(R.id.et_message)
        conversationContainer = view.findViewById(R.id.conversation_view)
        conversationsContainer = view.findViewById(R.id.conversations_view)
        emptyConversationsText = view.findViewById(R.id.tv_empty_conversations)
        convTitle = view.findViewById(R.id.tv_conv_title)

        setupConversationsList()
        setupMessagesListPlaceholder()
        bindViewModel()


        setupUiActions()

        viewModel.loadConversations()


        val args = arguments
        val userId = args?.getLong("openConversationWith", -1L) ?: -1L
        val username = args?.getString("username") ?: ""
        if (userId > 0) {
            openConversationByUserId(userId, username)
        }

        parentFragmentManager.setFragmentResultListener("openConversation", viewLifecycleOwner) { _, bundle ->
            val userId = bundle.getLong("userId", -1L)
            val username = bundle.getString("username") ?: ""
            if (userId > 0) {

                val conv = allConversations.firstOrNull { it.userId == userId }
                if (conv != null) {
                    openConversationByUserId(conv.userId, conv.name)
                } else {
                    openConversationByUserId(userId, username)
                }
            }
        }

    }

    private fun setupConversationsList() {
        conversationAdapter = ConversationAdapter { conversation ->
            openConversation(conversation)
        }
        conversationsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = conversationAdapter
        }
    }

    private fun setupMessagesListPlaceholder() {
        messagesRv.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun bindViewModel() {
        viewModel.conversations.observe(viewLifecycleOwner, Observer { list ->
            allConversations = list ?: emptyList()
            conversationAdapter.submitList(allConversations)
            emptyConversationsText.isVisible = allConversations.isEmpty()
        })

        viewModel.messages.observe(viewLifecycleOwner, Observer { msgs ->
            val msgsList = msgs ?: emptyList()

            val convUserId = currentConversationUserId
            if (convUserId != null) {
                if (messageAdapter == null || messageAdapter?.conversationUserId != convUserId) {
                    messageAdapter = MessageAdapter(convUserId)
                    messagesRv.adapter = messageAdapter
                }
                messageAdapter?.submitList(msgsList)
                messagesRv.post {
                    if (msgsList.isNotEmpty()) {
                        messagesRv.scrollToPosition(msgsList.size - 1)
                    }
                }
            }
        })

        viewModel.sentMessage.observe(viewLifecycleOwner, Observer { sent ->

            currentConversationUserId?.let { viewModel.loadConversation(it) }
            messageInput.setText("")
        })
    }

    private fun setupUiActions() {
        searchEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterConversations(s?.toString() ?: "")
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })



        newChatBtn.setOnClickListener {
            // replace the current fragment with UsersFragment (full screen)
            parentFragmentManager.beginTransaction()
                .replace(R.id.home_fragment_container, UsersFragment()) // use your host activity's container id
                .addToBackStack(null)
                .commit()
        }




        backBtn.setOnClickListener { showConversationsList() }

        sendBtn.setOnClickListener {
            val text = messageInput.text.toString().trim()
            val userId = currentConversationUserId
            if (text.isNotEmpty() && userId != null) {
                viewModel.sendMessage(userId, text)
            } else if (userId == null) {
                Toast.makeText(requireContext(), "Select a conversation first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filterConversations(query: String) {
        if (query.isBlank()) {
            conversationAdapter.submitList(allConversations)
            return
        }
        val filtered = allConversations.filter { conv ->
            val name = conv.username
            val last = conv.lastMessage
            name.contains(query, ignoreCase = true) || last.contains(query, ignoreCase = true)
        }
        conversationAdapter.submitList(filtered)
    }




    private fun openConversationByUserId(userId: Long, title: String? = null) {
        currentConversationUserId = userId
        convTitle.text = title ?: "Conversation"
        viewModel.loadConversation(userId)
        showConversationView()
    }

    private fun openConversation(conversation: ConversationItem) {
        currentConversationUserId = conversation.userId
        convTitle.text = conversation.name
        viewModel.loadConversation(conversation.userId)
        showConversationView()
        startPolling(conversation.userId)
    }

    private fun showConversationView() {
        conversationsContainer.isVisible = false
        conversationContainer.isVisible = true
    }

    private fun showConversationsList() {
        stopPolling()
        conversationContainer.isVisible = false
        conversationsContainer.isVisible = true
        messageInput.setText("")
        currentConversationUserId = null
        messageAdapter = null
        messagesRv.adapter = null
    }


    private var pollingJob: Job? = null

    private fun startPolling(userId: Long) {
        stopPolling()
        pollingJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                viewModel.loadConversation(userId)
                delay(2000) // poll every 3s (adjust as needed)
            }
        }
    }

    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

}