package com.example.frontend.View


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.frontend.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNav = findViewById<BottomNavigationView>(R.id.home_bottom_nav)

        // Load default fragment
        loadFragment(MessagesFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_messages ->
                    loadFragment(MessagesFragment())

                R.id.nav_trajets ->
                    loadFragment(TrajetsFragment())

                R.id.nav_reservations ->
                    loadFragment(ReservationsFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_fragment_container, fragment)
            .commit()
    }
}
