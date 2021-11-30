package org.d3ifcool.aspirin.data.viewmodel.sosialmedia

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3ifcool.aspirin.data.viewmodel.authentication.UserLiveData
import org.d3ifcool.aspirin.data.model.sosialmedia.PostingData
import org.d3ifcool.aspirin.data.network.Repo

class PostingViewModel : ViewModel() {
    val authUser = UserLiveData()

    private val repo = Repo()
    private var postingStatus = repo.getPostingStatus()
    val locationStatus = MutableLiveData<Boolean>()

    fun fetchPostingData(): LiveData<MutableList<PostingData>> {
        val mutableData = MutableLiveData<MutableList<PostingData>>()
        repo.getPostingData().observeForever {
            mutableData.value = it
        }
        return mutableData
    }

    fun postData(
        username: String,
        judul: String,
        lokasi: String,
        deskripsi: String,
        currentDate: String,
        photoUri: Uri
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.postData(username, judul, lokasi, deskripsi, currentDate, photoUri)
            }
        }
    }

    fun getLocationRequest(): LocationRequest {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 500
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        return mLocationRequest
    }

    fun getLocationCallback(): LocationCallback {
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        locationStatus.postValue(true)
                    } else {
                        locationStatus.postValue(false)
                    }
                }
            }
        }
        return mLocationCallback
    }

    fun getPostingStatus(): LiveData<Boolean> {
        return postingStatus
    }

    fun getLocationStatus(): LiveData<Boolean> {
        return locationStatus
    }
}