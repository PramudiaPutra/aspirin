package org.d3ifcool.aspirin.data.viewmodel.sosialmedia

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3ifcool.aspirin.data.model.comment.Comment
import org.d3ifcool.aspirin.data.viewmodel.authentication.UserLiveData
import org.d3ifcool.aspirin.data.model.sosialmedia.PostingData
import org.d3ifcool.aspirin.data.network.Repo

class PostingViewModel : ViewModel() {
    val authUser = UserLiveData()

    private val repo = Repo()
    private var postingStatus = repo.getPostingStatus()

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
        photoUri: Uri,
        comments: List<Comment>
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.postData(username, judul, lokasi, deskripsi, currentDate, photoUri, comments)
            }
        }
    }

    fun getPostingStatus(): LiveData<Boolean> {
        return postingStatus
    }
}