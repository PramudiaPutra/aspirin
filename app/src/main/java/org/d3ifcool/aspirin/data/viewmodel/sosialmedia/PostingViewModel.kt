package org.d3ifcool.aspirin.data.viewmodel.sosialmedia

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3ifcool.aspirin.data.viewmodel.authentication.UserLiveData
import org.d3ifcool.aspirin.data.model.sosialmedia.PostingData
import org.d3ifcool.aspirin.data.network.Repo

class PostingViewModel : ViewModel() {
    val authUser = UserLiveData()

    private val repo = Repo()
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

//    fun postData(postingData: PostingData){
//        viewModelScope.launch {
//            withContext(Dispatchers.IO){
//                repo.postData(postingData)
//            }
//        }
//    }
}