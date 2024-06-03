package com.example.earthquakeqazaqedition.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.earthquakeqazaqedition.R
import com.example.earthquakeqazaqedition.databinding.ActivityMainBinding
import com.example.earthquakeqazaqedition.ui.EarthquakeListFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(EarthquakeListFragment())
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.list_all -> {
                    replaceFragment(EarthquakeListFragment())
                    true
                }
                R.id.home -> {
                    replaceFragment(EarthquakeListFragment())
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