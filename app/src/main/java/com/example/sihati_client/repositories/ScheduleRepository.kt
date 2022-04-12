package com.example.sihati_client.repositories

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.sihati_client.R
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject

class ScheduleRepository {

    var  model = Schedule();
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var user: MutableLiveData<User> = MutableLiveData<User>()
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        getProfile()
    }

    fun getSchedules() : MutableLiveData<Schedule> {
        val data = MutableLiveData<Schedule>()
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("Schedule").document("Profile1");

        ref.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
            data.setValue(model)
        }
        return data;
    }

    fun getProfile(){
        Log.d("test", user.toString())
        val db = FirebaseFirestore.getInstance()
        val ref = auth.currentUser?.let { db.collection("User").document(it.uid) }

        ref?.addSnapshotListener { snapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let{
                Log.d("exeptions",it.message.toString())
                return@addSnapshotListener
            }
            snapshot?.let{
                user.value = it.toObject<User>()
                Log.d("test","I'm done with seting the data ")
            }
        }
        Log.d("test", user.value?.id.toString())

    }
}
