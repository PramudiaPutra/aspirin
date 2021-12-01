package org.d3ifcool.aspirin.data.network

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
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
        username: String,
        judul: String,
        lokasi: String,
        deskripsi: String,
        currentDate: String,
        photoUri: Uri,
        lat: Double?,
        lon: Double?
    ) {

        storageReference =
            Firebase.storage.reference
                .child("uploads")
                .child("camera/${File(photoUri.path).name}")

        database = FirebaseDatabase.getInstance().reference
        storageReference.putFile(photoUri)
            .addOnSuccessListener {

                storageReference.downloadUrl
                    .addOnSuccessListener { uri ->

                        val postingData = PostingData(
                            username,
                            judul,
                            lokasi,
                            deskripsi,
                            currentDate,
                            uri.toString(),
                            lat,
                            lon
                        )

                        database.child("postingan")
                            .child(database.push().key.toString())
                            .setValue(postingData)

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

    fun getPostingStatus(): LiveData<Boolean> {
        return postingStatus
    }
}