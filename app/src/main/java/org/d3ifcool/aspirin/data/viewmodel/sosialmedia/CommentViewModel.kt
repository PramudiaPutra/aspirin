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
import org.d3ifcool.aspirin.data.model.comment.CommentData
import org.d3ifcool.aspirin.data.model.sosialmedia.PostingData
import org.d3ifcool.aspirin.data.network.Repo
import org.d3ifcool.aspirin.data.viewmodel.authentication.UserLiveData

class CommentViewModel : ViewModel() {

    private val repo = Repo()

    fun fetchCommentData(key: String): LiveData<MutableList<CommentData>> {
        val mutableData = MutableLiveData<MutableList<CommentData>>()
        repo.getCommentData(key).observeForever {
            mutableData.value = it
        }
        return mutableData
    }
}