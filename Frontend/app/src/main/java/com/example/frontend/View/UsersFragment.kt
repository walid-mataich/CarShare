// UsersFragment.kt
package com.example.frontend.View

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R

import com.example.frontend.ViewModel.ChatViewModel
import com.example.frontend.model.UserItem
import kotlinx.coroutines.launch

class UsersFragment : Fragment() {

    private val viewModel: ChatViewModel by lazy { ChatViewModel() }

    private lateinit var rvUsers: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var adapter: UserAdapter

    private var allUsers: List<UserItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Use the layout you provided (save it as fragment_users_list.xml)
        return inflater.inflate(R.layout.fragment_users_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvUsers = view.findViewById(R.id.rv_users)
        etSearch = view.findViewById(R.id.et_search_user)

        adapter = UserAdapter { user ->
            // send result back to MessagesFragment
            val bundle = Bundle().apply {
                putLong("userId", user.id)
                putString("username", user.username)
            }
            setFragmentResult("openConversation", bundle)
            parentFragmentManager.popBackStack()
        }

        rvUsers.layoutManager = LinearLayoutManager(requireContext())
        rvUsers.adapter = adapter

        loadUsers()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val q = s?.toString()?.trim() ?: ""
                if (q.isBlank()) {
                    adapter.submitList(allUsers)
                } else {
                    adapter.submitList(allUsers.filter { it.username.contains(q, ignoreCase = true) })
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            try {
                val users = viewModel.getAllUsers() // suspend function in your ChatViewModel
                allUsers = users
                adapter.submitList(allUsers)
            } catch (e: Exception) {
                allUsers = emptyList()
                adapter.submitList(allUsers)
            }
        }
    }
}