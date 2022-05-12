package com.example.sihati_client.viewModels

import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.sihati_client.database.User
import com.example.sihati_client.repositories.AuthenticationRepository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AuthenticationRepository = AuthenticationRepository(application)
    val userData: MutableLiveData<FirebaseUser?> = repository.firebaseUserMutableLiveData

    fun register(
        email: String?,
        pass: String?,
        id: String?,
        name: String?,
        number: String,
        activity: Activity
    ) {
        repository.register(email, pass, id, name, number, activity)
    }

    fun signIn(
        email: String?,
        pass: String?,
        activity: Activity
    ) {
        repository.login(email, pass, activity)
    }

    fun signOut(requireActivity: Activity) {
        repository.signOut(requireActivity)
    }

    fun updateUser(user: User) {
        repository.updateUser(user)
    }

    fun recoverPassword(
        dialog: BottomSheetDialog,
        email: String,
        progressDialog: ProgressDialog,
        context: Context
    ) {
        repository.recoverPassword(dialog, email, progressDialog, context)
    }
}