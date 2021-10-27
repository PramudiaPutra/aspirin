package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.PostingData
import org.d3ifcool.aspirin.databinding.FragmentStoryBinding
import org.d3ifcool.aspirin.ui.home.viewmodel.PostingViewModel


class StoryFragment : Fragment() {

    private lateinit var binding: FragmentStoryBinding
    private lateinit var list: ArrayList<PostingData>
    private lateinit var myadapter: SosialMediaAdapter
    private val viewModel : PostingViewModel by lazy {
        ViewModelProvider(this).get(PostingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoryBinding.inflate(layoutInflater, container, false)

//        list = ArrayList()
//        list.clear()
//
//        database = FirebaseDatabase.getInstance("https://aspirin-aspirasi-indonesia-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("postingan")
//        getPostingan(database)

        myadapter = SosialMediaAdapter(this)
        with(binding.recyclerView){
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true

            layoutManager = linearLayoutManager
            observeData()
            setHasFixedSize(true)
            adapter = myadapter
        }

        binding.fab.setOnClickListener {
            val intent = Intent(context, PostingStoryActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    private fun observeData(){
        viewModel.fetchPostingData().observe(
            viewLifecycleOwner, {
                myadapter.setListData(it)
                myadapter?.notifyDataSetChanged()
            }
        )
    }

//    private fun getPostingan(databaseReference: DatabaseReference){
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                for (snapshot in dataSnapshot.children) {
//                    val stories = snapshot.getValue(PostingData::class.java)
//                    if (stories != null) {
//                        list.add(stories)
//                    }
//                }
//
//                with(binding.recyclerView) {
//                    val linearLayoutManager = LinearLayoutManager(context)
//                    linearLayoutManager.reverseLayout = true
//                    linearLayoutManager.stackFromEnd = true
//
//                    layoutManager = linearLayoutManager
//                    adapter = SosialMediaAdapter(list)
//                    setHasFixedSize(true)
//                    adapter?.notifyDataSetChanged()
//                }
//
//            }
//            override fun onCancelled(error: DatabaseError) {}
//        })
//    }


}