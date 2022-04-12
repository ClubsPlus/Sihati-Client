package com.example.sihati_client.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.database.User
import com.example.sihati_client.repositories.ScheduleRepository


class ScheduleViewModel : ViewModel() {
    private val mRepository = ScheduleRepository()

    var profile: MutableLiveData<User>? = null
    private var Schedules: MutableLiveData<Schedule>? = null

    fun init() {
        profile = mRepository.user
        Schedules = mRepository.getSchedules()
    }

    fun getSchedule(): LiveData<Schedule>? {
        return Schedules
    }
}