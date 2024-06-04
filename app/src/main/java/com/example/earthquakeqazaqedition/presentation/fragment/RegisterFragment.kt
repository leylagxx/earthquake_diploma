package com.example.earthquakeqazaqedition.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.earthquakeqazaqedition.R
import com.example.earthquakeqazaqedition.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        with(binding){
            registerButton.setOnClickListener {
                val username = username.text.toString()
                val email = email.text.toString()
                val password = password.text.toString()
                registerUser(username, email, password)
            }
            signInText.setOnClickListener {
                replaceFragment(LoginFragment())
            }
        }
    }
    private fun registerUser(username: String, email: String, password: String) {
        if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val user = hashMapOf(
                            "username" to username,
                            "email" to email
                        )
                        userId?.let {
                            firestore.collection("users").document(it).set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
                                    replaceFragment(LoginFragment())

                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(requireContext(), "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.popBackStack()
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

}