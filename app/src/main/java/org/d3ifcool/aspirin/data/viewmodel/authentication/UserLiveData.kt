package org.d3ifcool.aspirin.data.viewmodel.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class UserLiveData : LiveData<FirebaseUser?>() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    var authState = MutableLiveData<Boolean>()
    var verifiedState = MutableLiveData<Boolean>()

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

    fun login(email: String, password: String) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                if (user!!.isEmailVerified) {
                    authState.postValue(true)
                    verifiedState.postValue(true)
                } else {
                    firebaseAuth.signOut()
                    authState.postValue(false)
                    verifiedState.postValue(false)
                    errorMessage = "akun anda belum ter verifikasi, silahkan cek email anda"
                }
            } else {
                authState.postValue(false)
                verifiedState.postValue(false)
                errorMessage = task.exception?.message.toString()
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser

                val addUserName = UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(username)
                    .build()

                user?.updateProfile(addUserName)
                user?.sendEmailVerification()?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        firebaseAuth.signOut()
                        authState.postValue(true)
                    } else {
                        authState.postValue(false)
                        errorMessage = task.exception?.message.toString()
                    }
                }
            } else {
                authState.postValue(false)
                errorMessage = task.exception?.message.toString()
            }
        }
    }

    fun getAuthStatus(): LiveData<Boolean> {
        return authState
    }

    fun getVerifiedStatus(): LiveData<Boolean> {
        return verifiedState
    }

    fun getErrMessage(): String {
        return errorMessage
    }
}