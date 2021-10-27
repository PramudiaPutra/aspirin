package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.aspirin.databinding.FragmentStoryBinding
import org.d3ifcool.aspirin.ui.home.viewmodel.PostingViewModel


class StoryFragment : Fragment() {

    private lateinit var binding: FragmentStoryBinding
    private lateinit var myadapter: SosialMediaAdapter
    private val viewModel : PostingViewModel by lazy {
        ViewModelProvider(this).get(PostingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryBinding.inflate(layoutInflater, container, false)

        myadapter = SosialMediaAdapter()
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
}