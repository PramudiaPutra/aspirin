package org.d3ifcool.aspirin.data.viewmodel.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*


class UserLiveData : LiveData<FirebaseUser?>() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var authState = MutableLiveData<Boolean>()
    private var verifiedState = MutableLiveData<Boolean>()
    private var resetPasswordState = MutableLiveData<Boolean>()

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
                errorMessage(task)
                authState.postValue(false)
                verifiedState.postValue(false)
//                errorMessage = task.exception?.message.toString()
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
                        errorMessage(task)
                    }
                }
            } else {
                authState.postValue(false)
                errorMessage(task)
            }
        }
    }

    fun resetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                resetPasswordState.postValue(true)
            } else {
                resetPasswordState.postValue(false)
                errorMessage = when ((task.exception as FirebaseAuthException).errorCode) {
                    "ERROR_INVALID_EMAIL" -> "Format email salah"
                    "ERROR_USER_NOT_FOUND" -> "Email anda belum terdaftar"
                    else -> task.exception?.message.toString()
                }
            }
        }
    }

    fun getAuthStatus(): LiveData<Boolean> {
        return authState
    }

    fun getVerifiedStatus(): LiveData<Boolean> {
        return verifiedState
    }

    fun getResetPasswordStatus(): LiveData<Boolean> {
        return resetPasswordState
    }

    fun getErrMessage(): String {
        return errorMessage
    }

    private fun errorMessage(task: Task<AuthResult>) {
        errorMessage = when ((task.exception as FirebaseAuthException).errorCode) {
            "ERROR_INVALID_EMAIL" -> "Format email salah"
            "ERROR_WRONG_PASSWORD" -> "Password salah"
            "ERROR_USER_NOT_FOUND" -> "Email anda belum terdaftar"
            "ERROR_EMAIL_ALREADY_IN_USE" -> "Email sudah terdaftar"
            else -> task.exception?.message.toString()
        }
    }
}