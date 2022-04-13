package com.example.sihati_client.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.database.User
import com.example.sihati_client.repositories.ScheduleRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ScheduleViewModel : ViewModel() {
    private val mRepository = ScheduleRepository()

    var profile: MutableLiveData<User>? = null
    var schedules: MutableLiveData<List<Schedule>?>? = null

    fun init() {
        profile = mRepository.user
        schedules = mRepository.schedules
    }

    fun updateScheduleWithDate(date:String){
        Log.d("test","I'm in the updateScheduleWithDate viewmodel")
        schedules?.value = emptyList()
        mRepository.schedules.value = emptyList()
        Log.d("test","after cleaning the list in the viewmodel size="+ schedules?.value!!.size.toString())
        mRepository.getSchedules(date)
    }
}