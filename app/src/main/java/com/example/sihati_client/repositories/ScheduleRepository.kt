package com.example.sihati_client.repositories

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.sihati_client.R
import com.example.sihati_client.adapters.ScheduleAdapter
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ScheduleRepository {

    var  model = Schedule();
    private lateinit var instance: ScheduleRepository

    fun getMyInstance(): ScheduleRepository{
        if (instance==null){
            instance = ScheduleRepository();
        }
        return instance;
    }

    fun getData() : MutableLiveData<Schedule>? {
        val data = MutableLiveData<Schedule>();
        val db = FirebaseFirestore.getInstance();
        val ref = db.collection("users").document("Profile1");

        ref.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
//            model.(documentSnapshot.getString("firstName"));
//            model.setLassetFirstNametName(documentSnapshot.getString("lastName"));
//            model.setEmail(documentSnapshot.getString("email"));
            data.setValue(model);
        }
        return data;
    }
}