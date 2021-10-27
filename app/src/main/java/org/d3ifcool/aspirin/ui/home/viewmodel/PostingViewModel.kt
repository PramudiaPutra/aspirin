package org.d3ifcool.aspirin.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.PostingData
import org.d3ifcool.aspirin.ui.home.network.Repo

class PostingViewModel() : ViewModel() {
    private val repo = Repo()
    fun fetchPostingData():LiveData<MutableList<PostingData>>{
        val mutableData = MutableLiveData<MutableList<PostingData>>()
        repo.getPostingData().observeForever{
            mutableData.value = it
        }
        return mutableData
    }
}