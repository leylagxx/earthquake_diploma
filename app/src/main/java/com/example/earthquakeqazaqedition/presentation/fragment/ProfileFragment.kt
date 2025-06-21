package com.example.earthquakeqazaqedition.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.earthquakeqazaqedition.R
import com.example.earthquakeqazaqedition.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.navigation.fragment.findNavController

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val inputContainer = binding.root.findViewById<View>(R.id.inputContainer)
        val resultContainer = binding.root.findViewById<View>(R.id.resultContainer)

        val longitudeEditText = binding.root.findViewById<EditText>(R.id.longitudeEditText)
        val latitudeEditText = binding.root.findViewById<EditText>(R.id.latitudeEditText)
        val depthEditText = binding.root.findViewById<EditText>(R.id.depthEditText)
        val detectButton = binding.root.findViewById<Button>(R.id.detectButton)
        val resultMagnitude = binding.root.findViewById<TextView>(R.id.resultMagnitude)
        val backButton = binding.root.findViewById<Button>(R.id.backButton)

        detectButton.setOnClickListener {
            val longitude = longitudeEditText.text.toString().toDoubleOrNull()
            val latitude = latitudeEditText.text.toString().toDoubleOrNull()
            val depth = depthEditText.text.toString().toIntOrNull()

            if (longitude == null || latitude == null || depth == null) {
                Toast.makeText(
                    requireContext(),
                    "Барлық мәліметтер енгізілмеді",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val magnitudeType = if (depth < 70) {
                    "Төмен магнитудалы жерсілкініс"
                } else {
                    "Жоғары магнитудалы жерсілкініс"
                }

                resultMagnitude.text = magnitudeType

                inputContainer.visibility = View.GONE
                resultContainer.visibility = View.VISIBLE
            }
        }

        backButton.setOnClickListener {
            resultContainer.visibility = View.GONE
            inputContainer.visibility = View.VISIBLE
        }
    }
}
