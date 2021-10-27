package org.d3ifcool.aspirin.ui.home.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.PostingData

class Repo {
    private lateinit var database: DatabaseReference

    fun getPostingData():LiveData<MutableList<PostingData>>{
        val dataMutableList = MutableLiveData<MutableList<PostingData>>()
        database = FirebaseDatabase.getInstance("https://aspirin-aspirasi-indonesia-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("postingan")
        val list = mutableListOf<PostingData>()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {
                    val stories = snapshot.getValue(PostingData::class.java)
                    if (stories != null) {
                        list.add(stories)
                    }
                }
                dataMutableList.value = list
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        return dataMutableList
    }

    fun postData(postingData: PostingData){
        database = FirebaseDatabase.getInstance("https://aspirin-aspirasi-indonesia-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        database.child("postingan").child(database.push().key.toString()).setValue(postingData)
    }
}