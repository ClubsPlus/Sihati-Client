package com.example.sihati_client.repositories

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.sihati_client.database.User
import com.example.sihati_client.pages.authPages.AuthActivity
import com.example.sihati_client.pages.mainPage.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.messaging.FirebaseMessaging
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
                Toast.makeText(activity,"Compte créé avec succès",Toast.LENGTH_LONG).show()
                val mainActivity = MainActivity()
                activity.startActivity(Intent(activity,mainActivity::class.java))
              }
        }catch (e: Exception){
            withContext(Dispatchers.Main) {
                Log.d("exception","error: "+e.message.toString())
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
                    FirebaseMessaging.getInstance().token
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (task.result != null && !TextUtils.isEmpty(task.result)) {
                                    val thisUser: User = document.toObject()!!
                                    thisUser.token = task.result!!
                                    updateUser(thisUser)
                                }
                            }
                        }
                    break
                }
            }
            if(succes==0){
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, "l'email ou le mot de passe n'est pas correct", Toast.LENGTH_LONG).show()
                    signOut(activity)
                }
            }

        }catch (e:Exception){
            Log.d("exception",e.message.toString())
        }
    }

    fun signOut(requireActivity: Activity) {
        auth.signOut()
        requireActivity.startActivity(Intent(requireActivity,AuthActivity::class.java))
    }

    fun updateUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        val userQuery = userCollectionRef.document(auth.currentUser!!.uid).get().await()
        if(userQuery!=null){
            try {
                userCollectionRef.document(auth.currentUser!!.uid).set(
                    user,
                    SetOptions.merge()
                ).await()
            }catch (e:Exception){
                Log.d("exception","error: "+e.message.toString())
            }

        }else{
            Log.d("exception","error: the retrieving query is empty")
        }
    }
}