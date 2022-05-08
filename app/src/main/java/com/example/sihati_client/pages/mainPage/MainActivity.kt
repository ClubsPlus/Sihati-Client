package com.example.sihati_client.pages.mainPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sihati_client.R
import com.example.sihati_client.pages.authPages.AuthActivity
import com.example.sihati_client.pages.mainPage.fragments.AppointmentFragment
import com.example.sihati_client.pages.mainPage.fragments.HealthStatusFragment
import com.example.sihati_client.pages.mainPage.fragments.SchedulesFragment
import com.example.sihati_client.pages.mainPage.fragments.TestHistoryFragment
import com.example.sihati_client.viewModels.AuthViewModel
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {

    lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        /*setup the bottomNavigationView*/
        val bottomNavigationView = findViewById<ChipNavigationBar>(R.id.bottomNavigationView)

        // Check if user is signed in (non-null) and update UI accordingly.
        val viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        )[AuthViewModel::class.java]

        viewModel.userData.observe(this) { firebaseUser ->
            if (firebaseUser == null) {
                startActivity(Intent(this, AuthActivity::class.java))
            }
        }

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
                R.id.schedules -> {
                    fragment = SchedulesFragment()
                }
                R.id.appointment -> {
                    fragment = AppointmentFragment()
                }
                R.id.testHistory -> {
                    fragment = TestHistoryFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
        }
    }

}