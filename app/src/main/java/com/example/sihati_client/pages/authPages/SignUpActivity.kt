package com.example.sihati_client.pages.authPages

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sihati_client.R
import com.example.sihati_client.databinding.ActivitySignUpBinding
import com.example.sihati_client.pages.mainPage.MainActivity
import com.example.sihati_client.viewModels.AuthViewModel


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        // Check if user is signed in (non-null) and update UI accordingly.
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        )[AuthViewModel::class.java]

        viewModel.userData.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        binding.login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signup.setOnClickListener {
            if(binding.name.text.toString().isNotEmpty()
                &&binding.number.text.toString().isNotEmpty()
                &&binding.id.text.toString().isNotEmpty()
                &&binding.email.text.toString().isNotEmpty()
                &&binding.password.text.toString().isNotEmpty()){
                viewModel.register(binding.email.text.toString(),
                    binding.password.text.toString(),
                    binding.id.text.toString(),
                    binding.name.text.toString(),
                    binding.number.text.toString(),
                    this)
            }else
                Toast.makeText(this,"fill your fields plz", Toast.LENGTH_SHORT).show()
        }
    }
}