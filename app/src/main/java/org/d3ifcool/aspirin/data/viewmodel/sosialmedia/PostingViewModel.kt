package org.d3ifcool.aspirin.data.viewmodel.sosialmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3ifcool.aspirin.data.model.sosialmedia.MapData
import org.d3ifcool.aspirin.data.viewmodel.authentication.UserLiveData
import org.d3ifcool.aspirin.data.model.sosialmedia.PostingData
import org.d3ifcool.aspirin.data.network.Repo

class PostingViewModel : ViewModel() {
    val authUser = UserLiveData()

    private val repo = Repo()
    private var postingStatus = repo.getPostingStatus()
    val locationStatus = MutableLiveData<Boolean>()
    val locationCoordinate = MutableLiveData<LatLng>()

    fun fetchPostingData(): LiveData<MutableList<PostingData>> {
        val mutableData = MutableLiveData<MutableList<PostingData>>()
        repo.getPostingData().observeForever {
            mutableData.value = it
        }
        return mutableData
    }

    fun postData(
        postingData: PostingData,
        mapData: MapData?
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.postData(postingData, mapData)
            }
        }
    }

    fun getLocationRequest(): LocationRequest {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 5000
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
                        locationCoordinate.postValue(LatLng(location.latitude, location.longitude))
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

    fun getCoordinate(): LiveData<LatLng> {
        return locationCoordinate
    }
}