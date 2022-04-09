package com.example.sihati_client.pages.mainPage.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.sihati_client.R
import com.example.sihati_client.database.User
import com.example.sihati_client.databinding.FragmentHealthStatusBinding
import com.example.sihati_client.pages.authPages.LoginActivity
import com.example.sihati_client.viewModels.AuthViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HealthStatusFragment : Fragment() {

    private lateinit var binding: FragmentHealthStatusBinding
    private lateinit var currentUserRef :DocumentReference
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHealthStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[AuthViewModel::class.java]
        //this button is temporary used to logout
        binding.settings.setOnClickListener {
            viewModel.signOut(requireActivity())
        }

        val user = Firebase.auth.currentUser
        user?.let{
            val id = it.uid
            currentUserRef = Firebase.firestore.collection("User").document(id)
        }
        subscribeToRealtimeUpdates()

    }

    private fun subscribeToRealtimeUpdates(){
        currentUserRef.addSnapshotListener{ querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let{
                Toast.makeText(requireActivity(),it.message,Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapshot?.let{
                val user = it.toObject<User>()
                binding.name.text = user?.name
                binding.id.text = user?.id
                binding.status.text = user?.status
                when(user?.status){
                    "Positive" -> Glide.with(this).load(R.drawable.logo_red).into(binding.logo)
                    "Negative" -> Glide.with(this).load(R.drawable.logo_green).into(binding.logo)
                    "Pending" -> Glide.with(this).load(R.drawable.logo_grey).into(binding.logo)
                    "Not Tested" -> Glide.with(this).load(R.drawable.logo_yellow).into(binding.logo)
                }
            }
        }
    }
}