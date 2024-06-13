package com.example.earthquakeqazaqedition.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.earthquakeqazaqedition.R
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

    companion object {
        private const val PREFS_NAME = "filter_prefs"
        private const val PREF_KEY_MAGNITUDE = "pref_key_magnitude"
    }

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

        binding.filterButton.setOnClickListener {
            val filterFragment = FilterBottomSheetFragment()
            filterFragment.show(parentFragmentManager, FilterBottomSheetFragment.TAG)
        }

        parentFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            if (fragment is FilterBottomSheetFragment) {
                fragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                        super.onFragmentDetached(fm, f)
                        if (f is FilterBottomSheetFragment) {
                            fetchEarthquakes()
                            fm.unregisterFragmentLifecycleCallbacks(this)
                        }
                    }
                }, false)
            }
        }
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
        // Load saved magnitude filter from SharedPreferences
        val savedMagnitude = sharedPreferences.getFloat(PREF_KEY_MAGNITUDE, 0.0f)
        viewModel.fetchEarthquakes(savedMagnitude.toDouble())
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
}
