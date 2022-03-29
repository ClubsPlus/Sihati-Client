package com.example.sihati_client.mainPage

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sihati_client.R
import com.example.sihati_client.mainPage.fragments.HealthStatusFragment
import com.example.sihati_client.mainPage.fragments.SchedulesFragment
import com.example.sihati_client.mainPage.fragments.TestHistoryFragment
import com.google.android.material.navigation.NavigationBarView
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
        bottomNavigationView.setItemSelected(R.id.healthStatusFragment)
        //display the selected fragment using setOnItemSelectedListener function
        bottomNavigationView.setOnItemSelectedListener{
            when (it) {
                R.id.healthStatusFragment -> {
                    fragment = HealthStatusFragment()
                }
                R.id.testHistoryFragment -> {
                    fragment = TestHistoryFragment()
                }
                R.id.schedulesFragment -> {
                    fragment = SchedulesFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
        }
    }

}