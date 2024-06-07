package com.example.earthquakeqazaqedition.presentation.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.earthquakeqazaqedition.R
import com.example.earthquakeqazaqedition.databinding.ActivityMainBinding
import com.example.earthquakeqazaqedition.presentation.fragment.EarthquakeListFragment
import com.example.earthquakeqazaqedition.presentation.fragment.FilterBottomSheetFragment
import com.example.earthquakeqazaqedition.presentation.fragment.LoginFragment
import com.example.earthquakeqazaqedition.presentation.fragment.MapsFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.FirebaseApp


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(EarthquakeListFragment())
        binding.filterButton.setOnClickListener {
            val filterFragment = FilterBottomSheetFragment()
            filterFragment.show(supportFragmentManager, FilterBottomSheetFragment.TAG)
        }
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.list_all -> {
                    replaceFragment(EarthquakeListFragment())
                    true
                }
                R.id.profile -> {
                    replaceFragment(LoginFragment())
                    true
                }
                else -> true
            }
        }


    }
    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        supportFragmentManager.popBackStack()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}