package com.example.earthquakeqazaqedition.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.earthquakeqazaqedition.adapter.EarthquakeAdapter
import com.example.earthquakeqazaqedition.databinding.FragmentEarthquakeListBinding
import com.example.earthquakeqazaqedition.network.ApiClient
import com.example.earthquakeqazaqedition.viewmodel.EarthquakeListState
import com.example.earthquakeqazaqedition.viewmodel.EarthquakeListViewModel

class EarthquakeListFragment : Fragment() {

    private lateinit var binding: FragmentEarthquakeListBinding
    private val viewModel: EarthquakeListViewModel by lazy {
        ViewModelProvider(
            this,
            EarthquakeListViewModel.Provider(service = ApiClient.apiService)
        ).get<EarthquakeListViewModel>(EarthquakeListViewModel::class.java)

    }
    private var earthquakeAdapter: EarthquakeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEarthquakeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        earthquakeAdapter = EarthquakeAdapter()
        binding.earthquakeList.adapter = earthquakeAdapter
        observeViewModel()
        viewModel.fetchEarthquakes()
    }

    private fun observeViewModel() {
        viewModel.earthquakeListState.observe(viewLifecycleOwner) { earthquakes ->
            when (earthquakes) {
                is EarthquakeListState.Success -> {
                    earthquakeAdapter?.submitList(earthquakes.items)
                }

                else -> {}
            }
        }
    }
}
