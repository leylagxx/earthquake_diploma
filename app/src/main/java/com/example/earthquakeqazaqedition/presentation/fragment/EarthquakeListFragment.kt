package com.example.earthquakeqazaqedition.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.earthquakeqazaqedition.data.network.ApiClient
import com.example.earthquakeqazaqedition.presentation.adapter.EarthquakeAdapter
import com.example.earthquakeqazaqedition.databinding.FragmentEarthquakeListBinding
import com.example.earthquakeqazaqedition.databinding.FragmentFilterBottomSheetBinding
import com.example.earthquakeqazaqedition.presentation.viewmodel.EarthquakeListState
import com.example.earthquakeqazaqedition.presentation.viewmodel.EarthquakeListViewModel

class EarthquakeListFragment : Fragment() {

    private lateinit var binding: FragmentEarthquakeListBinding
    private lateinit var viewModel: EarthquakeListViewModel
    private var earthquakeAdapter: EarthquakeAdapter? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var filterBinding: FragmentFilterBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEarthquakeListBinding.inflate(inflater, container, false)
        filterBinding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUI()
        observeViewModel()
        fetchEarthquakes()
        setupScrollListener()
        setupFilterButton()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            EarthquakeListViewModel.Provider(service = ApiClient.apiService)
        ).get(EarthquakeListViewModel::class.java)
    }

    private fun setupUI() {
        earthquakeAdapter = EarthquakeAdapter()
        binding.earthquakeList.layoutManager = LinearLayoutManager(context)
        binding.earthquakeList.adapter = earthquakeAdapter
    }

    private fun observeViewModel() {
        viewModel.earthquakeListState.observe(viewLifecycleOwner) { earthquakes ->
            when (earthquakes) {
                is EarthquakeListState.Loading -> {
                    earthquakeAdapter?.setLoading(earthquakes.isLoading)
                }
                is EarthquakeListState.Success -> {
                    earthquakeAdapter?.submitList(earthquakes.items)
                    earthquakeAdapter?.setLoading(false)
                }
                is EarthquakeListState.Error -> {
                    earthquakeAdapter?.setLoading(false)
                }
            }
        }
    }

    private fun fetchEarthquakes() {
        viewModel.fetchEarthquakes()
    }

    private fun setupScrollListener() {
        binding.earthquakeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    if (viewModel.isLoadingMoreItems.value == false) {
                        viewModel.loadMoreItems()
                    }
                }
            }
        })
    }

    private fun setupFilterButton() {
        filterBinding.btnFilter.setOnClickListener {
            viewModel.filterEarthquakes("m1") // Change this to the selected filter logic
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireContext().getSharedPreferences("filter_prefs", Context.MODE_PRIVATE)
    }
}
