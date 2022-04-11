package com.example.sihati_client.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.repositories.ScheduleRepository


class ScheduleViewModel : ViewModel() {

    private var mMainModel: MutableLiveData<Schedule>? = null
    private var mRepository: ScheduleRepository? = null

    fun init() {
        if (mMainModel != null) {
            return
        }
        mRepository = mRepository?.getMyInstance()
        mMainModel = mRepository?.getData()
    }

    fun getSchedule(): LiveData<Schedule?>? {
        return mMainModel
    }
}