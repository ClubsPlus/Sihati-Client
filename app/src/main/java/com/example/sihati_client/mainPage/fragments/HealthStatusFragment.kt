package com.example.sihati_client.mainPage.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.sihati_client.R
import com.example.sihati_client.database.User
import com.example.sihati_client.databinding.FragmentHealthStatusBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.StringBuilder

class HealthStatusFragment : Fragment() {

    private lateinit var binding: FragmentHealthStatusBinding
    private val userCollectionRef = Firebase.firestore.collection("User")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        val user = Firebase.auth.currentUser
        user?.let{
            val id = it.uid
            retrieveUserData(id)
        }
    }

    private fun retrieveUserData(id: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = userCollectionRef.document(id).get().await()
            val user = querySnapshot.toObject<User>()
            withContext(Dispatchers.Main){
                binding.name.text = user?.name
                binding.id.text = user?.id
                binding.status.text = user?.status
                when(user?.status){
                    "positive" -> binding.logo.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.logo_red))
                    "negative" -> binding.logo.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.logo_green))
                    "pending" -> binding.logo.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.logo_grey))
                    "Not Tested" -> binding.logo.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.logo_yellow))
                }
            }
        }catch (e:Exception){

        }
    }
}