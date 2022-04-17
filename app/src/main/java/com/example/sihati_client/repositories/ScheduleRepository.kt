package com.example.sihati_client.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sihati_client.database.Laboratory
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScheduleRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    var user: MutableLiveData<User> = MutableLiveData<User>()
    var schedules:  MutableLiveData<List<Schedule>?> = MutableLiveData<List<Schedule>?>()

    var laboratoryCollectionRef = firestore.collection("Laboratory")
    var laboratory: Laboratory? = null

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        getProfile()
        val currentDate= LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = currentDate.format(formatter)
        getSchedules(date)
    }

    fun getSchedules(date:String){
        Log.d("test","I'm in the getSchedules")
        val db = FirebaseFirestore.getInstance()
        val list  = ArrayList<Schedule>()
        db.collection("Schedule").whereEqualTo("date",date)
            .orderBy("time_Start", Query.Direction.ASCENDING)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, firebaseFirestoreException ->
            schedules.value = emptyList()
            list.clear()
            firebaseFirestoreException?.let{
                Log.d("exeptions","error: "+it.message.toString())
                return@addSnapshotListener
            }
            snapshot?.let{
                for(document in it){
                    list.add(document.toObject())
                }
                Log.d("test","after cleaning the list in the repository size="+ schedules?.value?.size.toString())
                schedules.value = list
                Log.d("test","I'm done with seting the schedule in the repository ")
            }
        }
    }

    fun getLaboratoryById(uid:String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = laboratoryCollectionRef.document(uid).get().await()
            val thisLaboratory = querySnapshot.toObject<Laboratory>()
            if (thisLaboratory != null) {
                laboratory = thisLaboratory
            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d("exeptions", e.message.toString())
            }
        }
    }

    fun getProfile(){
        val db = FirebaseFirestore.getInstance()
        val ref = auth.currentUser?.let { db.collection("User").document(it.uid) }
        ref?.addSnapshotListener { snapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let{
                Log.d("exeptions","error: "+it.message.toString())
                return@addSnapshotListener
            }
            snapshot?.let{
                user.value = it.toObject<User>()
            }
        }
    }
}
