package com.example.earthquakeqazaqedition.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.earthquakeqazaqedition.adapter.EarthquakeAdapter
import com.example.earthquakeqazaqedition.databinding.FragmentEarthquakeListBinding
import com.example.earthquakeqazaqedition.model.Earthquake
import com.example.earthquakeqazaqedition.model.EarthquakeResponse
import com.example.earthquakeqazaqedition.network.ApiClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 */
class EarthquakeListFragment : Fragment() {
    companion object {
        fun newInstance() = EarthquakeListFragment()

    }
    private var _binding : FragmentEarthquakeListBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy {
        EarthquakeAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEarthquakeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        getEarthquakes()
    }
    private fun setupUI(){
        with(binding) {
            earthquakeList.adapter = this@EarthquakeListFragment.adapter
        }
    }
    private fun getEarthquakes() {
        ApiClient.apiService.fetchEarthquakes().enqueue(object : Callback<List<EarthquakeResponse>> {
            override fun onResponse(call: Call<List<EarthquakeResponse>>, response: Response<List<EarthquakeResponse>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    println(data)
                    data?.let {
                        adapter.submitList(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<EarthquakeResponse>>, t: Throwable) {
            }
        })
    }


}