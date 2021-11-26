package org.d3ifcool.aspirin.data.viewmodel.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class UserLiveData : LiveData<FirebaseUser?>() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    var registerState = MutableLiveData<Boolean>()
    private var errorMessage = ""

    private val authStateListener = FirebaseAuth.AuthStateListener {
        value = it.currentUser
    }

    override fun onActive() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    fun register(username: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = firebaseAuth.currentUser

                val addUserName = UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(username)
                    .build()

                currentUser?.updateProfile(addUserName)
                firebaseAuth.signOut()
                registerState.postValue(true)
            } else {
                registerState.postValue(false)
                errorMessage = task.exception?.message.toString()
            }
        }
    }

    fun getRegisterStatus(): LiveData<Boolean> {
        return registerState
    }

    fun getErrMessage(): String {
        return errorMessage
    }
}