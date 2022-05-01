package com.example.sihati_client.pages.mainPage.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sihati_client.adapters.TestAdapter
import com.example.sihati_client.databinding.FragmentTestHistoryBinding
import com.example.sihati_client.viewModels.ScheduleViewModel
import com.example.sihati_client.viewModels.TestViewModel

class TestHistoryFragment : Fragment() {

    private lateinit var binding: FragmentTestHistoryBinding
    lateinit var scheduleViewModel: ScheduleViewModel
    lateinit var testViewModel: TestViewModel
    private lateinit var testAdapter :TestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTestHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ScheduleViewModel::class.java]

        testViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[TestViewModel::class.java]

        testViewModel.init()
        scheduleViewModel.init()

        recyclerViewSetup()
    }

    private fun recyclerViewSetup() {
        // on below line we are setting layout
        // manager to our recycler view.
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        // on below line we are initializing our adapter class.
        testAdapter = TestAdapter(requireActivity())

        scheduleViewModel.allSchedules.observe(requireActivity()){ list ->
            list?.let { schedules->
                // on below line we are updating our list.
                testAdapter.updateList(testAdapter.allTests,schedules)
                Log.d("test","size in the observer= "+ scheduleViewModel.allSchedules.value?.size.toString())
            }
        }

        testViewModel.testsReady?.observe(requireActivity()){ list ->
            list?.let { tests->
                // on below line we are updating our list.
                testAdapter.updateList(tests,testAdapter.allSchedules)
            }
        }

        // on below line we are setting
        // adapter to our recycler view.
        binding.recyclerView.adapter = testAdapter
        binding.recyclerView.setHasFixedSize(true)
        Log.d("test","size after the set= "+ testAdapter.allSchedules.size.toString())
    }
}