package com.example.sihati_client.repositories

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.sihati_client.database.Schedule
import com.example.sihati_client.database.Test
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

class TestRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var tests: MutableLiveData<List<Test>> = MutableLiveData<List<Test>>()
    var testsReady: MutableLiveData<List<Test>> = MutableLiveData<List<Test>>()
    var testCollectionRef = firestore.collection("Test")

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        getTests()
        getTestsReady()
    }


    fun getTests() {
        val list = ArrayList<Test>()

        testCollectionRef.addSnapshotListener { snapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Log.d("exeptions", "error: " + it.message.toString())
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

    fun getTestsReady() {
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
                Log.d("exeptions", "error: " + it.message.toString())
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

    fun getTestsWithDate(date: String) {
        val db = FirebaseFirestore.getInstance()
        val list = ArrayList<Test>()
        db.collection("Schedule").whereEqualTo("date", date)
            .orderBy("time_Start", Query.Direction.ASCENDING)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, firebaseFirestoreException ->
                tests.value = emptyList()
                list.clear()
                firebaseFirestoreException?.let {
                    Log.d("exeptions", "error: " + it.message.toString())
                    return@addSnapshotListener
                }
                snapshot?.let {
                    tests.value = emptyList()
                    for (document in it) {
                        if (document.toObject<Test>().result == "Positive" || document.toObject<Test>().result == "Negative")
                            list.add(document.toObject())
                    }
                    tests.value = list
                }
            }
    }

    fun createTest(test: Test, activity: Activity) = CoroutineScope(Dispatchers.IO).launch {
        try {
            testCollectionRef.add(test).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(activity, "successfully saved data", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}