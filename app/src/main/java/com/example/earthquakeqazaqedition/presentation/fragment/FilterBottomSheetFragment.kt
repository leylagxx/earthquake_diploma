package com.example.earthquakeqazaqedition.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.example.earthquakeqazaqedition.R
import com.example.earthquakeqazaqedition.data.network.ApiClient
import com.example.earthquakeqazaqedition.databinding.FragmentFilterBottomSheetBinding
import com.example.earthquakeqazaqedition.presentation.viewmodel.FilterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterBottomSheetBinding? = null
    private lateinit var viewModel: FilterViewModel

    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val TAG = "FilterBottomSheetFragment"
        private const val PREFS_NAME = "filter_prefs"
        private const val PREF_KEY_MAGNITUDE = "pref_key_magnitude"
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
                this,
                FilterViewModel.Provider(service = ApiClient.apiService)
        ).get(FilterViewModel::class.java)

        sharedPreferences = requireContext().getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
        )

        val radioGroup: RadioGroup = view.findViewById(R.id.mag_filter)
        val btnFilter: Button = view.findViewById(R.id.btnFilter)

        val savedMagnitude = sharedPreferences.getFloat(PREF_KEY_MAGNITUDE, 0.0f)
        when (savedMagnitude) {
            0.0f -> radioGroup.check(R.id.radioAllEarthquakes)
            1.0f -> radioGroup.check(R.id.radioM1Earthquakes)
            2.5f -> radioGroup.check(R.id.radioM25Earthquakes)
            4.5f -> radioGroup.check(R.id.radioM45Earthquakes)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val editor = sharedPreferences.edit()
            when (checkedId) {
                R.id.radioAllEarthquakes -> {
                    viewModel.setMagnitudeFilter(0.0)
                    editor.putFloat(PREF_KEY_MAGNITUDE, 0.0f)
                }
                R.id.radioM1Earthquakes -> {
                    viewModel.setMagnitudeFilter(1.0)
                    editor.putFloat(PREF_KEY_MAGNITUDE, 1.0f)
                }
                R.id.radioM25Earthquakes -> {
                    viewModel.setMagnitudeFilter(2.5)
                    editor.putFloat(PREF_KEY_MAGNITUDE, 2.5f)
                }
                R.id.radioM45Earthquakes -> {
                    viewModel.setMagnitudeFilter(4.5)
                    editor.putFloat(PREF_KEY_MAGNITUDE, 4.5f)
                }
            }
            editor.apply()
        }

        btnFilter.setOnClickListener {
            viewModel.applyFilters()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
