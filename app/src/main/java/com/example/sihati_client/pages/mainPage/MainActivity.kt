package com.example.sihati_client.pages.mainPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sihati_client.R
import com.example.sihati_client.pages.mainPage.fragments.HealthStatusFragment
import com.example.sihati_client.pages.mainPage.fragments.SchedulesFragment
import com.example.sihati_client.pages.mainPage.fragments.TestHistoryFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {

    lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        /*setup the bottomNavigationView*/
        val bottomNavigationView = findViewById<ChipNavigationBar>(R.id.bottomNavigationView)

        //set a default value for the fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, HealthStatusFragment()).commit()
        bottomNavigationView.setItemSelected(R.id.healthStatus)
        //display the selected fragment using setOnItemSelectedListener function
        bottomNavigationView.setOnItemSelectedListener{
            when (it) {
                R.id.healthStatus -> {
                    fragment = HealthStatusFragment()
                }
                R.id.testHistory -> {
                    fragment = TestHistoryFragment()
                }
                R.id.schedules -> {
                    fragment = SchedulesFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
        }
    }

}