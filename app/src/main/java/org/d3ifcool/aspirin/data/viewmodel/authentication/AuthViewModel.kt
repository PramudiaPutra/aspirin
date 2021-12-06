package org.d3ifcool.aspirin.data.viewmodel.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    val auth = UserLiveData()
    private var authStatus = auth.getAuthStatus()
    private var verifiedCheck = auth.getVerifiedStatus()

    fun register(username: String, email: String, password: String) {
        auth.register(username, email, password)
    }

    fun login(email: String, password: String) {
        auth.login(email, password)
    }

    fun getRegisterStatus(): LiveData<Boolean> {
        return authStatus
    }

    fun getLoginStatus(): LiveData<Boolean> {
        return authStatus
    }

    fun getVerifiedStatus() : LiveData<Boolean> {
        return verifiedCheck
    }

    fun getErrMessage(): String {
        return auth.getErrMessage()
    }
}