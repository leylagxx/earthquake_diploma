package com.example.earthquakeqazaqedition.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.earthquakeqazaqedition.R
import com.example.earthquakeqazaqedition.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        val user = auth.currentUser
        user?.let {
            binding.emailTextView.text = it.email
            firestore.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        binding.usernameTextView.text = document.getString("username")
                    }
                }
        }

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            replaceFragment(LoginFragment())
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
