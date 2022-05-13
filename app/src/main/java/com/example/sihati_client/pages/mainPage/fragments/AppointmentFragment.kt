package com.example.sihati_client.pages.mainPage.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sihati_client.adapters.AppointementAdapter
import com.example.sihati_client.database.Test
import com.example.sihati_client.databinding.FragmentAppointmentBinding
import com.example.sihati_client.viewModels.ScheduleViewModel
import com.example.sihati_client.viewModels.TestViewModel


class AppointmentFragment : Fragment(), AppointementAdapter.TaskClickInterface {

    private lateinit var binding: FragmentAppointmentBinding
    private lateinit var testViewModel: TestViewModel
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var appointementAdapter :AppointementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        testViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[TestViewModel::class.java]

        testViewModel.init()

        scheduleViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ScheduleViewModel::class.java]

        recyclerViewSetup()
    }

    private fun recyclerViewSetup() {
        // on below line we are setting layout
        // manager to our recycler view.
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        // on below line we are initializing our adapter class.
        appointementAdapter = AppointementAdapter(requireActivity(),this,scheduleViewModel)

        // on below line we are setting
        // adapter to our recycler view.
        binding.recyclerView.adapter = appointementAdapter
        binding.recyclerView.setHasFixedSize(true)

        testViewModel.testsNotReady?.observe(requireActivity()){ list ->
            list?.let {
                // on below line we are updating our list.
                appointementAdapter.updateList(it)
            }

        }
    }

    override fun onClick(test: Test) {
        AlertDialog.Builder(requireContext( ))
            .setTitle("Annuler")
            .setMessage("Vous voulez vraiment annuler le rendez-vous?")
            .setPositiveButton("oui"
            ) { _, _ ->
                Log.d("test",test.schedule_id.toString())
                testViewModel.cancelAppointement(test)
                Toast.makeText(
                    requireContext(),
                    "Yaay",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("non", null).show()
    }
}