package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.dummy.PostingDummy
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.PostingData
import org.d3ifcool.aspirin.databinding.FragmentStoryBinding


class StoryFragment : Fragment() {

    private lateinit var binding: FragmentStoryBinding
    private var list: ArrayList<PostingData> = arrayListOf()
    private lateinit var myAdapter: SosialMediaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoryBinding.inflate(layoutInflater, container, false)
        list.addAll(PostingDummy.dataSet())
        myAdapter = SosialMediaAdapter(list)
        with(binding.recyclerView){
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true
            layoutManager = linearLayoutManager

            setHasFixedSize(true)
            adapter = myAdapter
        }
        return binding.root
    }
}