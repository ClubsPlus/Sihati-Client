package com.example.sihati_client.repositories

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.database.Test
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TestRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var testCollectionRef = firestore.collection("Test")
    var scheduleCollectionRef = firestore.collection("Schedule")

    var tests: MutableLiveData<List<Test>> = MutableLiveData<List<Test>>()
    var myTests: MutableLiveData<List<Test>> = MutableLiveData<List<Test>>()
    var testsReady: MutableLiveData<List<Test>> = MutableLiveData<List<Test>>()
    var testsNotReady: MutableLiveData<List<Test>> = MutableLiveData<List<Test>>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        getTests()
        getTestsReady()
        getTestsNotReady()
        getMyTests()
    }

    fun getTests() {
        val list = ArrayList<Test>()
        testCollectionRef.addSnapshotListener { snapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Log.d("exception", "error: " + it.message.toString())
                return@addSnapshotListener
            }
            snapshot?.let {
                for (document in it) {
                    list.add(document.toObject())
                }
                tests.value = list
            }
        }
    }

    private fun getMyTests() {
        val list = ArrayList<Test>()
        auth.currentUser?.let {
            testCollectionRef
                .whereEqualTo("user_id",auth.currentUser!!.uid)
                .addSnapshotListener { snapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    Log.d("exception", "error: " + it.message.toString())
                    return@addSnapshotListener
                }
                snapshot?.let {
                    for (document in it) {
                        list.add(document.toObject())
                    }
                    myTests.value = list
                }
            }
        }
    }

    private fun getTestsReady() {
        val db = FirebaseFirestore.getInstance()
        val list = ArrayList<Test>()
        val ref = auth.currentUser?.let {
            db.collection("Test")
                .whereEqualTo("user_id", auth.currentUser!!.uid)
        }
        ref?.addSnapshotListener { snapshot, firebaseFirestoreException ->
            list.clear()
            tests.value = emptyList()
            firebaseFirestoreException?.let {
                Log.d("exception", "error: " + it.message.toString())
                return@addSnapshotListener
            }
            snapshot?.let {
                for (document in it) {
                    if (document.toObject<Test>().result == "Positive" || document.toObject<Test>().result == "Negative")
                        list.add(document.toObject())
                }
                testsReady.value = list
            }
        }
    }

    private fun getTestsNotReady() {
        val db = FirebaseFirestore.getInstance()
        val list = ArrayList<Test>()
        val ref = auth.currentUser?.let {
            db.collection("Test")
                .whereEqualTo("user_id", auth.currentUser!!.uid)
                .whereEqualTo("result","Not Tested")
        }
        ref?.addSnapshotListener { snapshot, firebaseFirestoreException ->
            list.clear()
            tests.value = emptyList()
            firebaseFirestoreException?.let {
                Log.d("exception", "error: " + it.message.toString())
                return@addSnapshotListener
            }
            snapshot?.let {
                for (document in it) {
                        list.add(document.toObject())
                }
                testsNotReady.value = list
            }
        }
    }

    fun createTest(test: Test, activity: Activity) = CoroutineScope(Dispatchers.IO).launch {
        try {
            testCollectionRef.add(test).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(activity, "Données enregistrées avec succès", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun cancelAppointement(test: Test){
        testCollectionRef
            .whereEqualTo("laboratory_id",test.laboratory_id)
            .whereEqualTo("result",test.result)
            .whereEqualTo("user_id",test.user_id)
            .whereEqualTo("schedule_id",test.schedule_id)
            .whereEqualTo("date_end",test.date_end)
            .get().addOnSuccessListener {
                for(document in it){
                    testCollectionRef.document(document.id).delete()
                        .addOnSuccessListener {
                            scheduleCollectionRef.document(test.schedule_id!!).get().addOnSuccessListener { schedule ->
                                schedule?.let{
                                    val newSchedue: Schedule = schedule.toObject()!!
                                    newSchedue.person = newSchedue.person!!.toInt()-1
                                    try{
                                        scheduleCollectionRef.document (test.schedule_id!!).set(
                                            newSchedue,
                                            SetOptions.merge()
                                        )
                                    }catch(e: Exception) {
                                        Log.d("exception", "error: $e")
                                    }
                                }
                            }

                        }
                }
            }
    }
}