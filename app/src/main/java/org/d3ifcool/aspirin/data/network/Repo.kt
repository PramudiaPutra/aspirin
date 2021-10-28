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

    fun getPostingData(): LiveData<MutableList<PostingData>> {
        val dataMutableList = MutableLiveData<MutableList<PostingData>>()
        database =
            FirebaseDatabase.getInstance("https://aspirin-aspirasi-indonesia-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child(
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
        photoUri: Uri
    ) {

        storageReference =
            Firebase.storage.reference.child("uploads").child("camera/${File(photoUri.path).name}")
        database =
            FirebaseDatabase.getInstance("https://aspirin-aspirasi-indonesia-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        storageReference.putFile(photoUri).addOnSuccessListener {

            storageReference.downloadUrl.addOnSuccessListener { uri ->
                Log.i("file url", photoUri.toString())


                val postingData = PostingData(
                    username,
                    judul,
                    lokasi,
                    deskripsi,
                    currentDate,
                    uri.toString()
                )

                database.child("postingan").child(database.push().key.toString())
                    .setValue(postingData)

            }
        }


    }

//    fun postData(postingData: PostingData){
//        database = FirebaseDatabase.getInstance("https://aspirin-aspirasi-indonesia-default-rtdb.asia-southeast1.firebasedatabase.app").reference
//        database.child("postingan").child(database.push().key.toString()).setValue(postingData)
//    }
}