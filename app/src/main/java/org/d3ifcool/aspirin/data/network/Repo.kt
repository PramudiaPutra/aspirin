package org.d3ifcool.aspirin.data.network

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.aspirin.data.model.sosialmedia.MapData
import org.d3ifcool.aspirin.data.model.sosialmedia.PostingData
import java.io.File

class Repo {
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    var postingStatus = MutableLiveData<Boolean>()

    fun getPostingData(): LiveData<MutableList<PostingData>> {
        val dataMutableList = MutableLiveData<MutableList<PostingData>>()
        database =
            FirebaseDatabase.getInstance().reference.child(
                "postingan"
            )
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

    fun postData(
        postingData: PostingData,
        mapData: MapData?,
    ) {

        storageReference =
            Firebase.storage.reference
                .child("uploads")
                .child("camera/${File(postingData.photoUrl).name}")

        database = FirebaseDatabase.getInstance().reference
        val pushKey = database.push().key.toString()
        postingData.photoUrl?.let {
            storageReference.putFile(it.toUri())
                .addOnSuccessListener {

                    storageReference.downloadUrl
                        .addOnSuccessListener { uri ->

                            database.child("postingan")
                                .child(pushKey)
                                .setValue(postingData)

                            if (mapData != null) {
                                database.child("location")
                                    .child(pushKey)
                                    .setValue(mapData)
                            }

                            postingStatus.postValue(true)
                        }
                        .addOnFailureListener {
                            postingStatus.postValue(false)
                        }
                }
                .addOnFailureListener {
                    postingStatus.postValue(false)
                }
        }
    }

    fun getPostingStatus(): LiveData<Boolean> {
        return postingStatus
    }
}