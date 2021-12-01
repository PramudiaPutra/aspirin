package org.d3ifcool.aspirin.data.network

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.aspirin.data.model.comment.Comment
import org.d3ifcool.aspirin.data.model.sosialmedia.PostingData
import java.io.File
import java.util.*

class Repo {

    private lateinit var database: DatabaseReference
    val user = FirebaseAuth.getInstance().currentUser
    private lateinit var groupId: String
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
        comments: List<Comment>
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
                            comments
                        )

                        database.child("postingan")
//                            .child(database.push().key.toString())
                            .child(UUID.randomUUID().toString())
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

    fun getComments(): LiveData<MutableList<Comment>> {
        val dataMutableList = MutableLiveData<MutableList<Comment>>()

        Log.d("IDKU",UUID.randomUUID().toString() )
        database = FirebaseDatabase.getInstance().reference.child(
            "postingan"
        ).child(UUID.randomUUID().toString()).child("comments")

        val list = mutableListOf<Comment>()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {
                    val comments = snapshot.getValue(Comment::class.java)
                    if (comments != null) {
                        list.add(comments)
                    }
                }
                dataMutableList.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return dataMutableList
    }
}