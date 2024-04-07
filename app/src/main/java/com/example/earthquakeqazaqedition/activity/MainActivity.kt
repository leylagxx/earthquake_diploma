package com.example.earthquakeqazaqedition.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.earthquakeqazaqedition.R
import com.example.earthquakeqazaqedition.databinding.ActivityMainBinding
import com.example.earthquakeqazaqedition.fragment.EarthquakeListFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container_view, EarthquakeListFragment.newInstance())
            .commit()

    }
}