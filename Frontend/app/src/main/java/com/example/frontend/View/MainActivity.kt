package com.example.frontend.View

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.frontend.R
import com.example.frontend.ViewModel.MainViewModel


class MainActivity : ComponentActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var getDataButton: Button

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleTextView = findViewById(R.id.title_textView)
        getDataButton = findViewById(R.id.getData_button)


        // OBSERVER LA DATA
        viewModel.data.observe(this) { responseData ->
            titleTextView.text = responseData.title
        }

        // OBSERVER LES ERREURS
        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }


        getDataButton.setOnClickListener {
            viewModel.loadData()
        }
    }
}
