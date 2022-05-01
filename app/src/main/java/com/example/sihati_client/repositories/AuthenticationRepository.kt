package com.example.sihati_client.repositories

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.sihati_client.database.User
import com.example.sihati_client.pages.authPages.LoginActivity
import com.example.sihati_client.pages.mainPage.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AuthenticationRepository(private val application: Application) {
    val firebaseUserMutableLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    private var firestore = FirebaseFirestore.getInstance()
    var userCollectionRef = firestore.collection("User")

    init {
        if (auth.currentUser != null) {
            firebaseUserMutableLiveData.postValue(auth.currentUser)
        }
    }

    fun register(email: String?, pass: String?,id: String?,name: String?,number: String,activity: Activity) {
        auth.createUserWithEmailAndPassword(email!!, pass!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser!!.uid
                saveUser(User(id,name,number),uid,activity)
            }else{
                Toast.makeText(application, task.exception?.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun saveUser(user: User, uid: String, activity: Activity) = CoroutineScope(Dispatchers.IO).launch{
        try{
            userCollectionRef.document(uid).set(user).await()
              withContext(Dispatchers.Main){
                Toast.makeText(activity,"account created successfully",Toast.LENGTH_LONG).show()
                val mainActivity = MainActivity()
                activity.startActivity(Intent(activity,mainActivity::class.java))
              }
        }catch (e: Exception){
            withContext(Dispatchers.Main) {
                Log.d("Test",e.message.toString())
            }
        }
    }

    fun login(email: String?, pass: String?,activity: Activity) {
        auth.signInWithEmailAndPassword(email!!, pass!!).addOnCompleteListener { task ->
            if (task.isSuccessful){
                retrieveUsers(auth.currentUser!!,activity)
                firebaseUserMutableLiveData.postValue(auth.currentUser)
            } else {
                Toast.makeText(application, task.exception?.message  , Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun retrieveUsers(user: FirebaseUser,activity: Activity) = CoroutineScope(Dispatchers.IO).launch {
        try {
            var succes = 0
            val querySnapshot = userCollectionRef.get().await()
            for(document in querySnapshot.documents){
                if(document.id==user.uid){
                    succes = 1
                    firebaseUserMutableLiveData.postValue(auth.currentUser)
                    break
                }
            }
            if(succes==0){
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, "email or password is not correct", Toast.LENGTH_LONG).show()
                    signOut(activity)
                }
            }

        }catch (e:Exception){

        }
    }

    fun signOut(requireActivity: Activity) {
        val loginActivity = LoginActivity()
        auth.signOut()
        requireActivity.startActivity(Intent(requireActivity,loginActivity::class.java))
    }

    fun updateUser(result: String) = CoroutineScope(Dispatchers.IO).launch {
        val userQuery = userCollectionRef.document(auth.currentUser!!.uid).get().await()
        if(userQuery!=null){
            try {
                userCollectionRef.document(auth.currentUser!!.uid).set(
                    User(userQuery.toObject<User>()!!.id,
                        userQuery.toObject<User>()!!.name,
                        userQuery.toObject<User>()!!.number,
                        result),
                    SetOptions.merge()
                ).await()
            }catch (e:Exception){
                Log.d("exeptions","error: "+e.message.toString())
            }

        }else{
            Log.d("exeptions","error: the retrieving query is empty")
        }
    }
}