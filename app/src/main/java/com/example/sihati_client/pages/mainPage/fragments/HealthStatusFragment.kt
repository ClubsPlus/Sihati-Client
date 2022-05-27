package com.example.sihati_client.pages.mainPage.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.sihati_client.R
import com.example.sihati_client.database.User
import com.example.sihati_client.databinding.FragmentHealthStatusBinding
import com.example.sihati_client.pages.mainPage.MainActivity
import com.example.sihati_client.viewModels.AuthViewModel
import com.example.sihati_client.viewModels.ScheduleViewModel
import com.example.sihati_client.viewModels.TestViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HealthStatusFragment : Fragment() {

    private lateinit var binding: FragmentHealthStatusBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var testViewModel: TestViewModel
    private lateinit var scheduleViewModel: ScheduleViewModel

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

        authViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[AuthViewModel::class.java]

        testViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[TestViewModel::class.java]
        testViewModel.init()

        //this button is temporary used to logout
        binding.settings.setOnClickListener {
            authViewModel.signOut(requireActivity())
        }

        scheduleViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ScheduleViewModel::class.java]
        scheduleViewModel.init()

        scheduleViewModel.profile?.observe(requireActivity()){
            user -> UpdateViews(user)
        }
    }

    private fun UpdateViews(user: User?) {
        binding.name.text = user?.name
        binding.id.text = user?.id
        binding.status.text = user?.status
        when (user?.status) {
            "Positive" -> Glide.with(this).load(R.drawable.logo_red).into(binding.logo)
            "Negative" -> Glide.with(this).load(R.drawable.logo_green).into(binding.logo)
            "Pending" -> Glide.with(this).load(R.drawable.logo_yellow).into(binding.logo)
            "Not Tested" -> Glide.with(this).load(R.drawable.logo_grey).into(binding.logo)
        }
        if(testViewModel.myTests ==null){
            val newUser = User(user?.id,user?.name,user?.number,"Not Tested",user?.token)
            authViewModel.updateUser(newUser)
            binding.expirationDate.text = "non tester"
            binding.lastDate.text = "non tester"
        }else {
            testViewModel.myTests?.observe(requireActivity()) {
                val currentDate= LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val date = currentDate.format(formatter)
                for(test in it){
                    test.schedule_id?.let{
                        scheduleViewModel.getScheduleByIdAndSet(test.schedule_id!!, date = binding.lastDate, full = false)
                    }
                    if(test.date_end!!.isNotEmpty()&&test.date_end!![0].isDigit()&&
                        LocalDate.parse(date, formatter).isBefore(LocalDate.parse(test.date_end!!, formatter))){
                        Log.d("null","${test.date_end!!}<=$date")
                        binding.expirationDate.text = test.date_end!!.dropLast(5)
                    }else{
                        binding.expirationDate.text = "non tester"
                        val newUser = User(user?.id,user?.name,user?.number,"Not Tested",user?.token)
                        authViewModel.updateUser(newUser)
                    }
                    break
                }
            }
        }
    }
}