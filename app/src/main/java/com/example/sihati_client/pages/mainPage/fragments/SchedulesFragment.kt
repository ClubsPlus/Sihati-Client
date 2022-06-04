package com.example.sihati_client.pages.mainPage.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sihati_client.adapters.ScheduleAdapter
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.database.Test
import com.example.sihati_client.databinding.FragmentSchedulesBinding
import com.example.sihati_client.viewModels.ScheduleViewModel
import com.example.sihati_client.viewModels.TestViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar

class SchedulesFragment : Fragment(), ScheduleAdapter.OnClickInterface{

    private lateinit var binding: FragmentSchedulesBinding
    private lateinit var scheduleAdapter :ScheduleAdapter
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var testViewModel: TestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSchedulesBinding.inflate(inflater,container,false)
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

        scheduleViewModel.init()

        recyclerViewSetup()
        setupCalendar(binding.calander)
    }

    private fun recyclerViewSetup() {
        // on below line we are setting layout
        // manager to our recycler view.
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        // on below line we are initializing our adapter class.
        scheduleAdapter = ScheduleAdapter(requireActivity(), this,scheduleViewModel)

        // on below line we are setting
        // adapter to our recycler view.
        binding.recyclerView.adapter = scheduleAdapter
        binding.recyclerView.setHasFixedSize(true)

        scheduleViewModel.schedules?.observe(requireActivity()){ list ->
            list?.let {
                // on below line we are updating our list.
                scheduleAdapter.updateList(it)
            }

        }
    }

    private fun setupCalendar(collapsibleCalendar: CollapsibleCalendar){
        collapsibleCalendar.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onDayChanged() {

            }

            override fun onClickListener() {
                if(collapsibleCalendar.expanded){
                    collapsibleCalendar.collapse(400)
                }
                else{
                    collapsibleCalendar.expand(400)
                }
            }

            override fun onDaySelect() {
                val day: Day = collapsibleCalendar.selectedDay!!

                val thistoday = if(day.day<10) "0"+(day.day).toString() else day.day.toString()
                val thismonth= if(day.month + 1<10) "0"+(day.month+1).toString() else (day.month + 1).toString()
                val date = thistoday+"/"+thismonth+"/"+day.year
                scheduleViewModel.updateScheduleWithDate(date)
                scheduleViewModel.schedules?.observe(requireActivity()){ list ->
                    list?.let {
                        // on below line we are updating our list.
                        scheduleAdapter.updateList(it)
                    }
                }
            }

            override fun onItemClick(v: View) {

            }

            override fun onDataUpdate() {

            }

            override fun onMonthChange() {

            }

            override fun onWeekChange(position: Int) {

            }
        })
    }

    override fun onClick(schedule: Schedule) {
        val oldSchedule = Schedule(schedule.id,schedule.date,schedule.laboratory_id,schedule.limite,schedule.person,schedule.time_Start,schedule.time_end)
        val newSchedule = Schedule(schedule.id,schedule.date,schedule.laboratory_id,schedule.limite,schedule.person?.plus(1),schedule.time_Start,schedule.time_end)
        scheduleViewModel.updateSchedule(oldSchedule,newSchedule)
        val test = Test(schedule.laboratory_id,"Not Tested",
            scheduleViewModel.auth?.uid.toString(),
            schedule.id)
        Log.d("test",schedule.id.toString())
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+schedule.id.toString())
        testViewModel.createTest(test,requireActivity())
    }
}