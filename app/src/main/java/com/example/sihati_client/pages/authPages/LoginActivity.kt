package com.example.sihati_client.pages.authPages

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sihati_client.R
import com.example.sihati_client.databinding.ActivityLoginBinding
import com.example.sihati_client.pages.mainPage.MainActivity
import com.example.sihati_client.viewModels.AuthViewModel


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Check if user is signed in (non-null) and update UI accordingly.
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        )[AuthViewModel::class.java]

        viewModel.userData.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        binding.signup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.login.setOnClickListener {
            if(binding.email.text.toString().isNotEmpty()
                &&binding.password.text.toString().isNotEmpty()){
                viewModel.signIn(binding.email.text.toString(),
                    binding.password.text.toString(),this)
            }else Toast.makeText(this,"please fill your email and password", Toast.LENGTH_LONG).show()
        }
    }
}