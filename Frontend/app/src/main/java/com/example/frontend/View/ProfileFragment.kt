package com.example.frontend.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.frontend.R
import com.example.frontend.ViewModel.AuthViewModel
import com.example.frontend.ViewModel.ChatViewModel
import com.example.frontend.api.RetrofitInstance
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.getValue

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: AuthViewModel by lazy { AuthViewModel() }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTv = view.findViewById<TextView>(R.id.profileName)
        val emailTv = view.findViewById<TextView>(R.id.profileEmail)
        val logoutBtn = view.findViewById<Button>(R.id.btnLogout)
        val profileLetter = view.findViewById<TextView>(R.id.profileLetter)

        val prefs = requireContext()
            .getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        val name = prefs.getString("USER_NAME", "U") ?: "U"
        nameTv.text = name
        emailTv.text = prefs.getString("USER_EMAIL", "—")

        profileLetter.text = name.first().uppercase()



        nameTv.text = prefs.getString("USER_NAME", "—")
        emailTv.text = prefs.getString("USER_EMAIL", "—")

        logoutBtn.setOnClickListener {
            performLogout()
        }


        viewModel.logoutResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Logged out locally", Toast.LENGTH_SHORT).show()
            }

            navigateToMainActivity()
        }
    }

    private fun performLogout() {
        viewModel.logout(requireContext())
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        requireActivity().finish()
    }






}
