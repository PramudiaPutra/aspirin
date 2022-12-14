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
import com.google.firebase.database.DataSnapshot
import org.d3ifcool.aspirin.data.model.comment.CommentData

class Repo {
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    var postingStatus = MutableLiveData<Boolean>()
    var loadStory = MutableLiveData<Boolean>()
    var isStoryEmpty = MutableLiveData<Boolean>()

    fun getPostingData(): LiveData<MutableList<PostingData>> {
        val dataMutableList = MutableLiveData<MutableList<PostingData>>()
        database =
            FirebaseDatabase.getInstance().reference.child(
                "postingan"
            )
        val list = mutableListOf<PostingData>()

        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val stories = snapshot.getValue(PostingData::class.java)
                loadStory.postValue(true)
                if (stories != null) {
                    isStoryEmpty.postValue(true)
                    stories.key = snapshot.key
                    list.add(stories)
                    dataMutableList.value = list
                } else {
                    isStoryEmpty.postValue(false)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}

        })

        return dataMutableList
    }

    fun getCommentData(key: String): LiveData<MutableList<CommentData>> {
        val commentData = MutableLiveData<MutableList<CommentData>>()
        val commentList = mutableListOf<CommentData>()

        database = FirebaseDatabase.getInstance().reference.child("comments").child(key)
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val comments = snapshot.getValue(CommentData::class.java)
                if (comments != null) {
                    commentList.add(comments)
                    commentData.value = commentList
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}

        })

        return commentData
    }

    fun getMapData(): LiveData<MutableList<MapData>> {
        val mapData = MutableLiveData<MutableList<MapData>>()
        val mapList = mutableListOf<MapData>()

        database = FirebaseDatabase.getInstance().reference.child(
            "location"
        )
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val mapSnapshot = snapshot.getValue(MapData::class.java)
                    if (mapSnapshot != null) {
                        mapSnapshot.key = snapshot.key
                        mapList.add(mapSnapshot)
                    }
                }
                mapData.value = mapList
            }

            override fun onCancelled(error: DatabaseError) {}

        })
        return mapData
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
                            postingData.photoUrl = uri.toString()

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

    fun postComment(commentData: CommentData) {
        database = FirebaseDatabase.getInstance().reference
        val pushKey = database.push().key.toString()
        database.child("comments").child(commentData.key.toString()).child(pushKey)
            .setValue(commentData)
    }

    fun getPostingStatus(): LiveData<Boolean> {
        return postingStatus
    }

    fun getStoryStatus(): LiveData<Boolean> {
        return isStoryEmpty
    }

    fun getLoadStory(): LiveData<Boolean> {
        return loadStory
    }
}