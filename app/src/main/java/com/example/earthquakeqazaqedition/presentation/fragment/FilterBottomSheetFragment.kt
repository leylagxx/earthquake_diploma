package com.example.earthquakeqazaqedition.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.earthquakeqazaqedition.R
import com.example.earthquakeqazaqedition.databinding.FragmentFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences(
            "filter_prefs",
            Context.MODE_PRIVATE
        )

        setupRadioButtons()
        setupFilterButton()
    }

    private fun setupRadioButtons() {
        val selectedFilter = sharedPreferences.getString("selected_filter", "m45") ?: "m45"

        when (selectedFilter) {
            "all" -> binding.radioAllEarthquakes.isChecked = true
            "m1" -> binding.radioM1Earthquakes.isChecked = true
            "m25" -> binding.radioM25Earthquakes.isChecked = true
            "m45" -> binding.radioM45Earthquakes.isChecked = true
        }
    }

    private fun setupFilterButton() {
        binding.btnFilter.setOnClickListener {
            val selectedFilter = when (binding.magFilter.checkedRadioButtonId) {
                R.id.radioAllEarthquakes -> "all"
                R.id.radioM1Earthquakes -> "m1"
                R.id.radioM25Earthquakes -> "m25"
                R.id.radioM45Earthquakes -> "m45"
                else -> "all"
            }

            with(sharedPreferences.edit()) {
                putString("selected_filter", selectedFilter)
                apply()
            }
            dismiss()
        }
    }

    companion object {
        const val TAG = "FilterBottomSheetFragment"
    }
}
