package org.d3ifcool.aspirin.data.viewmodel.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    val auth = UserLiveData()
    private var authStatus = auth.getAuthStatus()
    private var verifiedCheck = auth.getVerifiedStatus()
    private var resetPasswordCheck = auth.getResetPasswordStatus()

    fun register(username: String, email: String, password: String) {
        auth.register(username, email, password)
    }

    fun login(email: String, password: String) {
        auth.login(email, password)
    }

    fun resetPassword(email: String) {
        auth.resetPassword(email)
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

    fun getResetPasswordStatus(): LiveData<Boolean> {
        return resetPasswordCheck
    }

    fun getErrMessage(): String {
        return auth.getErrMessage()
    }
}