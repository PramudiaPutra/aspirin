package org.d3ifcool.aspirin.data.viewmodel.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    val auth = UserLiveData()
    private var registerStatus = auth.getRegisterStatus()

    fun register(username: String, email: String, password: String) {
        auth.register(username, email, password)
    }

    fun getRegisterStatus(): LiveData<Boolean> {
        return registerStatus
    }

    fun getErrMessage(): String {
        return auth.getErrMessage()
    }
}