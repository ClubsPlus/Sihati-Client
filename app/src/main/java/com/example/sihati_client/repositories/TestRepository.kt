package com.example.sihati_client.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.database.Test
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

class TestRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    var tests: MutableLiveData<List<Test>> = MutableLiveData<List<Test>>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        getTests()
    }

    fun getTestsWithDate(date:String){
        val db = FirebaseFirestore.getInstance()
        val list  = ArrayList<Test>()
        db.collection("Schedule").whereEqualTo("date",date)
            .orderBy("time_Start", Query.Direction.ASCENDING)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, firebaseFirestoreException ->
                tests.value = emptyList()
                list.clear()
                firebaseFirestoreException?.let{
                    Log.d("exeptions","error: "+it.message.toString())
                    return@addSnapshotListener
                }
                snapshot?.let{
                    tests.value = emptyList()
                    for(document in it){
                        if(document.toObject<Test>().result=="Positive"||document.toObject<Test>().result=="Negative")
                            list.add(document.toObject())
                    }
                    tests.value = list
                }
            }
    }

    fun getTests(){
        val db = FirebaseFirestore.getInstance()
        val list  = ArrayList<Test>()
        val ref = auth.currentUser?.let { db.collection("Test")
            .whereEqualTo("user_id", auth.currentUser!!.uid)
            .whereEqualTo("result","Positive")
            .whereEqualTo("result","Negative")}
        ref?.addSnapshotListener { snapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let{
                Log.d("exeptions","error: "+it.message.toString())
                return@addSnapshotListener
            }
            snapshot?.let{
                tests.value = emptyList()
                for(document in it){
                    if(document.toObject<Test>().result=="Positive"||document.toObject<Test>().result=="Negative")
                        list.add(document.toObject())
                }
                tests.value = list
            }
        }
    }
}