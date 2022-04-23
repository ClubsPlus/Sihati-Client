package com.example.sihati_client.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sihati_client.database.Laboratory
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.database.User
import com.example.sihati_client.repositories.ScheduleRepository
import com.google.firebase.auth.FirebaseAuth


class ScheduleViewModel : ViewModel() {
    private val mRepository = ScheduleRepository()
    var auth: FirebaseAuth? = null
    var profile: MutableLiveData<User>? = null
    var schedules: MutableLiveData<List<Schedule>?>? = null
    var laboratory: Laboratory? = null
    var schedule: Schedule? = null

    fun init() {
        auth = mRepository.auth
        profile = mRepository.user
        schedules = mRepository.schedules
    }

    fun updateScheduleWithDate(date:String){
        schedules?.value = emptyList()
        mRepository.getSchedules(date)
    }

    fun getLaboratoryById(uid:String){
        mRepository.getLaboratoryById(uid)
        laboratory = mRepository.laboratory
    }

    fun getScheduleById(uid:String){
        mRepository.getScheduleById(uid)
        schedule = mRepository.schedule
    }

    fun updateSchedule(schedule: Schedule, newSchedule: Schedule){
        mRepository.updateSchedule(schedule,newSchedule)
    }
}