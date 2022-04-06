package com.example.sihati_client.mainPage.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sihati_client.R
import com.example.sihati_client.databinding.FragmentHealthStatusBinding
import com.firebase.ui.auth.AuthUI

class HealthStatusFragment : Fragment() {

    private lateinit var binding: FragmentHealthStatusBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHealthStatusBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //this button is temporary used to logout
        binding.settings.setOnClickListener {
            AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener {
                    activity?.finish()
                }
        }
    }
}