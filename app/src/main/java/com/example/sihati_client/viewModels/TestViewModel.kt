package com.example.sihati_client.viewModels

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sihati_client.database.Test
import com.example.sihati_client.repositories.TestRepository

class TestViewModel : ViewModel() {
    private val mRepository = TestRepository()
    var tests: MutableLiveData<List<Test>>? = null
    var testsReady: MutableLiveData<List<Test>>? = null
    var testsNotReady: MutableLiveData<List<Test>>? = null

    fun init() {
        tests = mRepository.tests
        testsReady = mRepository.testsReady
        testsNotReady = mRepository.testsNotReady
    }

    fun updateScheduleWithDate(date:String){
        mRepository.getTestsWithDate(date)
    }

    fun createTest(test: Test, activity: Activity){
        mRepository.createTest(test,activity)
    }
}