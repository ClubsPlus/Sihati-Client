package com.example.sihati_client.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sihati_client.database.Laboratory
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.database.User
import com.example.sihati_client.repositories.ScheduleRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ScheduleViewModel : ViewModel() {
    private val mRepository = ScheduleRepository()
    var auth: FirebaseAuth? = null
    var profile: MutableLiveData<User>? = null
    var schedules: MutableLiveData<List<Schedule>?>? = null
    var laboratory: Laboratory? = null

    fun init() {
        auth = mRepository.auth
        profile = mRepository.user
        schedules = mRepository.schedules
    }

    fun updateScheduleWithDate(date:String){
        Log.d("test","I'm in the updateScheduleWithDate viewmodel")
        schedules?.value = emptyList()
        Log.d("test","after cleaning the list in the viewmodel size="+ schedules?.value!!.size.toString())
        mRepository.getSchedules(date)
    }

    fun getLaboratoryById(uid:String){
        mRepository.getLaboratoryById(uid)
        laboratory = mRepository.laboratory
    }

    fun updateSchedule(schedule: Schedule, newSchedule: Schedule){
        mRepository.updateSchedule(schedule,newSchedule)
    }
}