package com.example.sihati_client.viewModels

import android.widget.TextView
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
    var allSchedules: MutableLiveData<List<Schedule>?> = MutableLiveData()
    var schedule: Schedule? = null

    var mylaboratory:  MutableLiveData<HashMap<String,Laboratory>?>? = null
    var mySchedule:  MutableLiveData<HashMap<String,Schedule>>? = null

    fun init() {
        auth = mRepository.auth
        profile = mRepository.user
        schedules = mRepository.schedules
        mySchedule = mRepository.mySchedule
        mylaboratory = mRepository.mylaboratory
        allSchedules = mRepository.allSchedules
    }

    fun updateScheduleWithDate(date:String){
        schedules?.value = emptyList()
        mRepository.getSchedules(date)
    }

    fun getLaboratoryByIdAndSet(uid:String,laboratoryName:TextView){
        mRepository.getLaboratoryById(uid,laboratoryName)
    }

    fun getScheduleByIdAndSet(uid:String,date:TextView,startTime:TextView?=null,endTime:TextView?=null,time:TextView?=null,full:Boolean?=null){
        mRepository.getScheduleByIdAndSet(uid,date,startTime,endTime,time,full)
    }

    fun updateSchedule(schedule: Schedule, newSchedule: Schedule){
        mRepository.updateSchedule(schedule,newSchedule)
    }
}