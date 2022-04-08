package com.example.sihati_client.pages.mainPage.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sihati_client.R
import com.example.sihati_client.adapters.ScheduleAdapter
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.databinding.FragmentSchedulesBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SchedulesFragment : Fragment(), ScheduleAdapter.OnClickInterface, TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: FragmentSchedulesBinding
    private var currentUserRef = Firebase.firestore.collection("Schedule")
    private lateinit var scheduleAdapter :ScheduleAdapter

    private var time = "Time"
    private var hour = 0
    private var minute = 0
    private var savedhour = "0"
    private var savedminute = "0"

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

        val currentDate= LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = currentDate.format(formatter)
        recyclerViewSetup(date)
        setupCalendar(binding.calander)

        /*setup the date and time pickers*/
        binding.time.setOnClickListener {
            getDateTimeCalendar()
            TimePickerDialog(context,R.style.DialogTheme,this,hour,minute,true).show()
        }
    }

    private fun recyclerViewSetup(date: String) {
        // on below line we are setting layout
        // manager to our recycler view.
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        // on below line we are initializing our adapter class.
        scheduleAdapter = ScheduleAdapter(requireActivity(), this)

        // on below line we are setting
        // adapter to our recycler view.
        binding.recyclerView.adapter = scheduleAdapter
        binding.recyclerView.setHasFixedSize(true)

        subscribeToRealtimeUpdates(date,time)
    }

    private fun subscribeToRealtimeUpdates(date:String,time:String){
        currentUserRef.whereEqualTo("date",date).orderBy("time_Start",Query.Direction.ASCENDING)
            .addSnapshotListener{ querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let{
                    Toast.makeText(requireActivity(),it.message, Toast.LENGTH_LONG).show()
                    Log.d("test",it.message.toString())
                    return@addSnapshotListener
                }
                if(querySnapshot?.size()!=0){
                    val schedules  = mutableListOf<Schedule>()
                    for (document in querySnapshot!!){
                        val schedule = document.toObject<Schedule>()
                        if(schedule.person!! < schedule.limite!!){
                            if (time[0].isDigit()){

                            }else{
                                schedule.id = document.id
                                Firebase.firestore.collection("Laboratory")
                                    .document(schedule.laboratory_id!!).get().addOnSuccessListener {
                                        schedule.laboratory_name = it.get("name").toString()
                                        schedules.add(schedule)
                                        scheduleAdapter.updateList(schedules)
                                    }
                            }
                        }
                    }
                }else{
                    scheduleAdapter.updateList(emptyList())
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
                time = "Time"
                binding.time.text= time
                subscribeToRealtimeUpdates(date,time)
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
    }

    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        savedhour = if(hour<10) "0$hour" else hour.toString()
        savedminute = if(minute<10) "0$minute" else minute.toString()
        binding.time.text = "$savedhour:$savedminute"
        time  = "$savedhour:$savedminute"
    }

    fun isNowBetweenDateTime(s: Date?, e: Date?): Boolean {
        val now = Date()
        return now.after(s) && now.before(e)
    }
}