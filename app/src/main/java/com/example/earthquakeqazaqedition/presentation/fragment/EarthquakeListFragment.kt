package com.example.earthquakeqazaqedition.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.earthquakeqazaqedition.presentation.adapter.EarthquakeAdapter
import com.example.earthquakeqazaqedition.databinding.FragmentEarthquakeListBinding
import com.example.earthquakeqazaqedition.data.network.ApiClient
import com.example.earthquakeqazaqedition.presentation.viewmodel.EarthquakeListState
import com.example.earthquakeqazaqedition.presentation.viewmodel.EarthquakeListViewModel

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
        setupUI()
        observeViewModel()
        viewModel.fetchEarthquakes()

    }
    private fun setupUI(){
        earthquakeAdapter = EarthquakeAdapter()
        binding.earthquakeList.adapter = earthquakeAdapter
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
